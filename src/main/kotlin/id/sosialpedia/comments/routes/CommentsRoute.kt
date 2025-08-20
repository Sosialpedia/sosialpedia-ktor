package id.sosialpedia.comments.routes

import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.comments.routes.model.ChildCommentRequest
import id.sosialpedia.comments.routes.model.CommentRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

fun Route.commentConfig() {
    val commentRepository by inject<CommentRepository>(CommentRepository::class.java)

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

    delete("/comment") {
        var httpStatusCode = HttpStatusCode.OK
        val commentId = call.request.queryParameters["commentId"]
            ?: throw IllegalArgumentException("commentId can't be blank")
        val postId = call.request.queryParameters["postId"]
            ?: throw IllegalArgumentException("postId can't be blank")
        val userId = call.request.queryParameters["userId"]
            ?: throw IllegalArgumentException("userId can't be blank")
        val result = commentRepository.deleteComment(commentId, postId, userId)
        if (result.isSuccess) {
            call.respond(
                httpStatusCode,
                WebResponse(
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
                    result.exceptionOrNull()?.localizedMessage,
                    httpStatusCode.value
                )
            )
        }
    }

    get("/comments/c") {
        var httpStatusCode = HttpStatusCode.OK
        val commentId = call.request.queryParameters["commentId"]
            ?: throw IllegalArgumentException("commentId can't be empty")
        call.request.queryParameters["userId"]
            ?: throw IllegalArgumentException("userId can't be empty")
        val result = commentRepository.getChildComments(commentId)
        if (result.isSuccess) {
            call.respond(
                httpStatusCode,
                WebResponse(httpStatusCode.description, result.getOrNull(), httpStatusCode.value)
            )
        } else {
            httpStatusCode = HttpStatusCode.NotAcceptable
            call.respond(
                httpStatusCode,
                WebResponse(
                    httpStatusCode.description,
                    result.exceptionOrNull()?.localizedMessage,
                    httpStatusCode.value
                )
            )

        }
    }
    post("/comment/c") {
        var httpStatusCode = HttpStatusCode.OK
        val childCommentRequest = call.receive<ChildCommentRequest>()
        val result = commentRepository.addChildComment(childCommentRequest)
        if (result.isSuccess) {
            call.respond(
                httpStatusCode,
                WebResponse(
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
                    result.exceptionOrNull()?.localizedMessage,
                    httpStatusCode.value
                )
            )
        }
    }
    delete("comment/c") {
        var httpStatusCode = HttpStatusCode.OK
        val childCommentId = call.request.queryParameters["childCommentId"]
            ?: throw IllegalArgumentException("childCommentId can't be empty")
        val commentId = call.request.queryParameters["commentId"]
            ?: throw IllegalArgumentException("commentId can't be empty")
        val userId = call.request.queryParameters["userId"]
            ?: throw IllegalArgumentException("userId can't be empty")
        val result = commentRepository.deleteChildComment(childCommentId, commentId, userId)
        if (result.isSuccess) {
            call.respond(
                httpStatusCode,
                WebResponse(
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
                    result.exceptionOrNull()?.localizedMessage,
                    httpStatusCode.value
                )
            )
        }
    }
}