/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdochub.document.api.delete

import io.github.perracodex.kopapi.dsl.operation.api
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kdochub.core.context.SessionContext
import kdochub.core.context.getContext
import kdochub.document.api.DocumentRouteApi
import kdochub.document.service.DocumentAuditService
import kdochub.document.service.DocumentService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.plugin.scope

@DocumentRouteApi
internal fun Route.deleteAllDocumentsRoute() {
    delete("/v1/document/") {
        val sessionContext: SessionContext = call.getContext()
        call.scope.get<DocumentAuditService> { parametersOf(sessionContext) }
            .audit(operation = "delete all")

        val service: DocumentService = call.scope.get<DocumentService> { parametersOf(sessionContext) }
        val deletedCount: Int = service.deleteAll()
        call.respond(status = HttpStatusCode.OK, message = deletedCount)
    } api {
        tags = setOf("Document")
        summary = "Delete all documents."
        description = "Delete all document entries."
        operationId = "deleteAllDocuments"
        response<Int>(status = HttpStatusCode.OK) {
            description = "The number of documents deleted."
        }
    }
}