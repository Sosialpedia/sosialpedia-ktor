package id.sosialpedia.attachments.routes

import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.attachments.routes.model.CreateAttachmentRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * Mengonfigurasi route untuk lampiran (attachment) dengan desain yang lebih baik.
 */
fun Route.configureAttachmentRoutes() {
    // Inject AttachmentRepository menggunakan Koin
    val attachmentRepository by inject<AttachmentRepository>(AttachmentRepository::class.java)

    route("/posts/{postId}/attachments") {
        /**
         * Endpoint untuk menambahkan satu atau lebih lampiran ke sebuah post spesifik.
         * Menerima List<CreateAttachmentRequest> di dalam body.
         */
        post {
            val postId = call.parameters["postId"]
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing postId parameter")

            try {
                // Menerima daftar lampiran dari request body.
                val requests = call.receive<List<CreateAttachmentRequest>>()
                val result = attachmentRepository.addAttachments(postId, requests)

                if (result.isSuccess) {
                    call.respond(
                        HttpStatusCode.Created, WebResponse(
                            message = "Attachments added successfully",
                            data = result.getOrNull(),
                            code = HttpStatusCode.Created.value
                        )
                    )
                } else {
                    // Lemparkan error dari Result untuk ditangkap oleh blok catch.
                    throw result.exceptionOrNull() ?: Exception("Unknown error while adding attachments")
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest, WebResponse(
                        message = e.localizedMessage ?: "Invalid request body or operation failed",
                        data = null,
                        code = HttpStatusCode.BadRequest.value
                    )
                )
            }
        }

        /**
         * Endpoint untuk mengambil semua lampiran dari sebuah post.
         */
        get {
            val postId = call.parameters["postId"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing postId parameter")

            try {
                val result = attachmentRepository.getAttachmentsForPost(postId)
                if (result.isSuccess) {
                    call.respond(
                        HttpStatusCode.OK, WebResponse(
                            message = "Attachments fetched successfully",
                            data = result.getOrNull(),
                            code = HttpStatusCode.OK.value
                        )
                    )
                } else {
                    throw result.exceptionOrNull() ?: Exception("Failed to fetch attachments")
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError, WebResponse(
                        message = e.localizedMessage ?: "An error occurred",
                        data = null,
                        code = HttpStatusCode.InternalServerError.value
                    )
                )
            }
        }
    }

    route("/attachments/{attachmentId}") {
        /**
         * Endpoint untuk menghapus sebuah lampiran spesifik berdasarkan ID-nya.
         */
        delete {
            val attachmentId = call.parameters["attachmentId"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing attachmentId parameter")

            try {
                // Otorisasi (apakah user ini boleh menghapus) sebaiknya dilakukan di sini
                // sebelum memanggil repository. Untuk saat ini, kita langsung panggil.
                val result = attachmentRepository.removeAttachment(attachmentId)

                if (result.isSuccess && result.getOrDefault(false)) {
                    call.respond(
                        HttpStatusCode.OK, WebResponse(
                            message = "Attachment successfully deleted",
                            data = true,
                            code = HttpStatusCode.OK.value
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound, WebResponse(
                            message = "Attachment not found or deletion failed",
                            data = false,
                            code = HttpStatusCode.NotFound.value
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError, WebResponse(
                        message = e.localizedMessage ?: "An error occurred",
                        data = null,
                        code = HttpStatusCode.InternalServerError.value
                    )
                )
            }
        }
    }
}