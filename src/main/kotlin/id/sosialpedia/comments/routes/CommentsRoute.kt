package id.sosialpedia.comments.routes

import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.comments.routes.model.CommentRequest
import id.sosialpedia.util.WebResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureCommentsRouting() {
    val commentRepository by inject<CommentRepository>()

    routing {
        get("/comments") {
            var httpStatusCode = HttpStatusCode.OK
            val postId = call.request.queryParameters["postId"] ?: throw NoSuchElementException("postId can't be empty")
            call.request.queryParameters["userId"] ?: throw NoSuchElementException()
            val result = commentRepository.getCommentsFromPost(postId)
            if (result.isSuccess) {
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        message = httpStatusCode.description,
                        data = result.getOrNull(),
                        code = httpStatusCode.value
                    )
                )
            } else {
                httpStatusCode = HttpStatusCode.NotAcceptable
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        message = httpStatusCode.description,
                        data = result.exceptionOrNull()?.localizedMessage,
                        code = httpStatusCode.value
                    )
                )
            }
        }
        post("/comment") {
            var httpStatusCode = HttpStatusCode.OK
            val commentRequest = call.receive<CommentRequest>()
            val result = commentRepository.addComment(commentRequest)
            if (result.isSuccess) {
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        message = httpStatusCode.description,
                        data = result.getOrNull(),
                        code = httpStatusCode.value
                    )
                )
            } else {
                httpStatusCode = HttpStatusCode.NotAcceptable
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        message = httpStatusCode.description,
                        data = result.exceptionOrNull()?.localizedMessage,
                        code = httpStatusCode.value
                    )
                )
            }
        }

        get("comments/reno") {
            var httpStatusCode = HttpStatusCode.OK
            val result = commentRepository.testingTransaction()
            if (result.isSuccess) {
                call.respond(
                    httpStatusCode,
                    WebResponse(httpStatusCode.description, result.getOrNull(), httpStatusCode.value)
                )
            } else {
                httpStatusCode = HttpStatusCode.NotAcceptable
                call.respond(
                    httpStatusCode,
                    WebResponse(httpStatusCode.description, result.exceptionOrNull()?.localizedMessage, httpStatusCode.value)
                )
            }
        }
    }
}