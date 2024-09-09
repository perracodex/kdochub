/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdoc.access.token.routing.operate

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kdoc.access.token.annotation.TokenAPI
import kdoc.access.token.routing.respondWithToken
import kdoc.access.token.service.AuthenticationTokenService

/**
 * Allows a client to refresh their existing JWT token. This endpoint does not require
 * Basic Authentication but does require a valid JWT token in the 'Authorization' header.
 * Depending on the state of the provided token (valid, expired, or invalid), it either
 * returns the same token, generates a new one, or denies access.
 *
 * See: [Ktor JWT Authentication Documentation](https://ktor.io/docs/server-jwt.html)
 *
 * See: [Basic Authentication Documentation](https://ktor.io/docs/server-basic-auth.html)
 */
@TokenAPI
internal fun Route.refreshTokenRoute() {
    // Endpoint for token refresh.
    // No Basic Authentication is required here, but an existing token's validity will be checked.
    // For example, in Postman set the endpoint and in the Headers add an Authorization key
    // with a 'Bearer' holding a previous valid token.
    post("auth/token/refresh") {
        val headers: Headers = call.request.headers

        AuthenticationTokenService.getState(headers = headers).let { result ->
            when (result) {
                is AuthenticationTokenService.TokenState.Valid -> {
                    // Token is still valid; return the same token to the client.
                    call.respond(status = HttpStatusCode.OK, message = result.token)
                }

                is AuthenticationTokenService.TokenState.Expired -> {
                    // Token has expired; generate a new token and respond with it.
                    call.respondWithToken()
                }

                is AuthenticationTokenService.TokenState.Invalid -> {
                    // Token is invalid; respond with an Unauthorized status.
                    call.respond(status = HttpStatusCode.Unauthorized, message = "Invalid token.")
                }
            }
        }
    }
}
