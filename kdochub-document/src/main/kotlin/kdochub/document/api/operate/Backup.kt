/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdochub.document.api.operate

import io.github.perracodex.kopapi.dsl.operation.api
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.perracodex.exposed.pagination.Page
import kdochub.base.context.SessionContext
import kdochub.base.context.getContext
import kdochub.document.api.DocumentRouteApi
import kdochub.document.model.Document
import kdochub.document.service.DocumentAuditService
import kdochub.document.service.DocumentService
import kdochub.document.service.manager.DownloadManager
import org.koin.core.parameter.parametersOf
import org.koin.ktor.plugin.scope

@DocumentRouteApi
internal fun Route.backupDocumentsRoute() {
    get("/v1/document/backup") {
        // Audit the backup action.
        val sessionContext: SessionContext = call.getContext()
        call.scope.get<DocumentAuditService> { parametersOf(sessionContext) }
            .audit(operation = "backup")

        // Get all documents.
        val documentService: DocumentService = call.scope.get<DocumentService> { parametersOf(sessionContext) }
        val documents: Page<Document> = documentService.findAll()
        if (documents.content.isEmpty()) {
            call.respond(status = HttpStatusCode.NoContent, message = "No documents found.")
            return@get
        }

        // Stream the backup to the client.
        val streamHandler: DownloadManager.StreamHandler = DownloadManager.prepareStream(
            documents = documents.content,
            decipher = false,
            archiveFilename = "backup",
            archiveAlways = true
        )
        DownloadManager.backupCountMetric.increment()
        call.response.header(HttpHeaders.ContentDisposition, streamHandler.contentDisposition.toString())
        call.respondOutputStream(contentType = streamHandler.contentType) {
            streamHandler.stream(this)
        }
    } api {
        tags = setOf("Document")
        summary = "Backup all documents."
        description = "Download a backup file containing all document entries."
        operationId = "backupDocuments"
        response(status = HttpStatusCode.NoContent) {
            description = "No documents found."
        }
    }
}
