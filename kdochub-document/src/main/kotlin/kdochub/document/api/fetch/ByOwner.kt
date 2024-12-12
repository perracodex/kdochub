/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdochub.document.api.fetch

import io.github.perracodex.kopapi.dsl.operation.api
import io.github.perracodex.kopapi.dsl.parameter.pathParameter
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.perracodex.exposed.pagination.Page
import io.perracodex.exposed.pagination.Pageable
import io.perracodex.exposed.pagination.getPageable
import kdochub.core.context.SessionContext
import kdochub.core.context.getContext
import kdochub.core.util.toUuid
import kdochub.document.api.DocumentRouteApi
import kdochub.document.model.Document
import kdochub.document.service.DocumentAuditService
import kdochub.document.service.DocumentService
import org.koin.core.parameter.parametersOf
import org.koin.ktor.plugin.scope
import kotlin.uuid.Uuid

@DocumentRouteApi
internal fun Route.findDocumentsByOwnerRoute() {
    get("/v1/document/owner/{owner_id}") {
        val ownerId: Uuid = call.parameters.getOrFail(name = "owner_id").toUuid()
        val pageable: Pageable? = call.getPageable()

        val sessionContext: SessionContext = call.getContext()
        call.scope.get<DocumentAuditService> { parametersOf(sessionContext) }
            .audit(operation = "find by owner", ownerId = ownerId, log = pageable?.toString())

        val service: DocumentService = call.scope.get<DocumentService> { parametersOf(sessionContext) }
        val documents: Page<Document> = service.findByOwnerId(ownerId = ownerId, pageable = pageable)
        call.respond(status = HttpStatusCode.OK, message = documents)
    } api {
        tags = setOf("Document")
        summary = "Find documents by owner."
        description = "Find all document entries by owner."
        operationId = "findDocumentsByOwner"
        pathParameter<Uuid>(name = "owner_id") {
            description = "The owner ID to find documents for."
        }
        response<Page<Document>>(status = HttpStatusCode.OK) {
            description = "The list of documents."
        }
    }
}