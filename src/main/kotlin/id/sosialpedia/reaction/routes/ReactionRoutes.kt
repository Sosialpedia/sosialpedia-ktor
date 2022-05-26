package id.sosialpedia.reaction.routes

import id.sosialpedia.reaction.domain.ReactionRepository
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
fun Route.configureReactionRoutes() {
    val reactionRepository by inject<ReactionRepository>(ReactionRepository::class.java)

    post("/reaction/like/post") {
        var httpStatusCode = HttpStatusCode.OK
        val userId =
            call.request.queryParameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        val postId =
            call.request.queryParameters["postId"] ?: throw IllegalArgumentException("postId can't be empty")
        val result = reactionRepository.addLikeToPost(postId, userId)
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
    delete("/reaction/like/post") {
        var httpStatusCode = HttpStatusCode.OK
        val userId =
            call.request.queryParameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        val postId =
            call.request.queryParameters["postId"] ?: throw IllegalArgumentException("postId can't be empty")
        val result = reactionRepository.removeLikeFromPost(postId, userId)
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
    post("/reaction/like/comment") {
        var httpStatusCode = HttpStatusCode.OK
        val userId =
            call.request.queryParameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        val commentId =
            call.request.queryParameters["commentId"] ?: throw IllegalArgumentException("commentId can't be empty")
        val result = reactionRepository.addLikeToComment(commentId, userId)
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
    delete("/reaction/like/comment") {
        var httpStatusCode = HttpStatusCode.OK
        val userId =
            call.request.queryParameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        val commentId =
            call.request.queryParameters["commentId"] ?: throw IllegalArgumentException("commentId can't be empty")
        val result = reactionRepository.removeLikeFromComment(commentId, userId)
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