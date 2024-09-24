/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdoc.access.token.api

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kdoc.access.credential.CredentialService
import kdoc.access.token.annotation.TokenAPI
import kdoc.access.token.service.AuthenticationTokenService
import kdoc.core.env.SessionContext
import kdoc.core.env.SessionContext.Companion.getContext
import kdoc.core.env.Tracer

/**
 * Application call extension function for responding with a JWT token.
 * Generates a new JWT token for the authenticated session and sends it as a response.
 * To be used only within the Token API.
 *
 * Responds with:
 * - OK (200) and the JWT token if generation is successful.
 * - Bad Request (400) with an error message if the [SessionContext] is invalid.
 * - Internal Server Error (500) with a general error message if an unexpected error occurs during token generation.
 */
@TokenAPI
internal suspend fun ApplicationCall.respondWithToken() {
    val result: Result<String> = runCatching {
        this.getContext()?.let { sessionContext ->
            return@runCatching AuthenticationTokenService.generate(sessionContext = sessionContext)
        } ?: throw IllegalArgumentException("Invalid actor. ${CredentialService.HINT}")
    }

    result.onFailure { e ->
        Tracer(ref = ApplicationCall::respondWithToken)
            .error(message = "Failed to generate token.", cause = e)

        when (e) {
            is IllegalArgumentException -> {
                this.respond(
                    status = HttpStatusCode.BadRequest,
                    message = "Invalid SessionContext. ${CredentialService.HINT}"
                )
            }

            else -> {
                this.respond(
                    status = HttpStatusCode.InternalServerError,
                    message = "Failed to generate token."
                )
            }
        }
    }.getOrThrow()

    if (result.isSuccess) {
        this.respond(status = HttpStatusCode.OK, message = result.getOrNull()!!)
    }
}
