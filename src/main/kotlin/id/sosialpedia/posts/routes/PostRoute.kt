package id.sosialpedia.posts.routes

import id.sosialpedia.posts.domain.PostRepository
import id.sosialpedia.posts.routes.model.CreatePostRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
fun Route.postConfig() {
    val postUserRepository by inject<PostRepository>(PostRepository::class.java)

    get("/posts") {
        val httpStatusCode = HttpStatusCode.OK
        val userId = call.request.queryParameters["userId"] ?: throw NoSuchElementException("userId is empty!")
        val result = postUserRepository.getAllPostByUserId(userId)
        call.respond(
            status = httpStatusCode,
            message = WebResponse(
                message = httpStatusCode.description,
                data = result,
                code = httpStatusCode.value
            )
        )
    }
    post("/post") {
        var httpStatusCode = HttpStatusCode.Created
        val postRequest =
            call.receiveOrNull<CreatePostRequest>() ?: throw NoSuchElementException("post request is empty")
        val result = postUserRepository.createPost(postRequest)
        if (result.isSuccess) {
            call.respond(
                status = httpStatusCode,
                message = WebResponse(
                    message = httpStatusCode.description,
                    data = result.getOrNull(),
                    code = httpStatusCode.value
                )
            )
        } else {
            httpStatusCode = HttpStatusCode.BadRequest
            call.respond(
                status = httpStatusCode,
                message = WebResponse<List<Int>>(
                    message = httpStatusCode.description,
                    data = emptyList(),
                    code = httpStatusCode.value
                )
            )
        }
    }
    delete("/post") {
        val httpStatusCode = HttpStatusCode.OK
        val postId = call.request.queryParameters["postId"] ?: throw NoSuchElementException("postId can't be empty")
        val userId = call.request.queryParameters["userId"] ?: throw NoSuchElementException("userId can't be empty")
        postUserRepository.deletePostById(
            userId = userId,
            postId = postId
        )
        call.respond(
            WebResponse(
                message = httpStatusCode.description,
                data = listOf("Successfully deleted"),
                code = httpStatusCode.value
            )
        )
    }
}