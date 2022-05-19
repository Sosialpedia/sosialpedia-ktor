package id.sosialpedia.attachments.routes

import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.attachments.routes.model.CreateAttachmentRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */
fun Application.configureAttachmentRoutes() {
    val attachmentRepository by inject<AttachmentRepository>(AttachmentRepository::class.java)

    routing {
        post("post/attachment") {
            var httpStatusCode = HttpStatusCode.OK
            val createAttachmentRequest = call.receive<CreateAttachmentRequest>()
            val result = attachmentRepository.addAttachment(createAttachmentRequest)
            if (result.isSuccess) {
                call.respond(
                    httpStatusCode, WebResponse(
                        httpStatusCode.description,
                        result.getOrNull(),
                        httpStatusCode.value
                    )
                )
            } else {
                httpStatusCode = HttpStatusCode.NotAcceptable
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        httpStatusCode.description,
                        result.exceptionOrNull()?.cause?.localizedMessage,
                        httpStatusCode.value
                    )
                )
            }
        }
        delete("post/attachment") {
            var httpStatusCode = HttpStatusCode.OK
            val attachmentId = call.request.queryParameters["attachmentId"] ?: throw IllegalArgumentException("attachmentId can't be empty")
            val referenceId = call.request.queryParameters["referenceId"] ?: throw IllegalArgumentException("referenceId can't be empty")
            val result = attachmentRepository.removeAttachment(attachmentId, referenceId)
            if (result.isSuccess) {
                call.respond(
                    httpStatusCode, WebResponse(
                        httpStatusCode.description,
                        result.getOrNull(),
                        httpStatusCode.value
                    )
                )
            } else {
                httpStatusCode = HttpStatusCode.NotAcceptable
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        httpStatusCode.description,
                        result.exceptionOrNull()?.cause?.localizedMessage,
                        httpStatusCode.value
                    )
                )
            }
        }
    }
}