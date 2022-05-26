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
fun Route.configurePostsRouting() {
    val postUserRepository by inject<PostRepository>(PostRepository::class.java)

    get("/posts") {
        val httpStatusCode = HttpStatusCode.OK
        val userId = call.request.queryParameters["userId"] ?: throw IllegalArgumentException("userId is empty!")
        val result = postUserRepository.getAllPostByUserId(userId)
        call.respond(httpStatusCode, WebResponse(httpStatusCode.description, result, httpStatusCode.value))
    }
    post("/post") {
        var httpStatusCode = HttpStatusCode.Created
        val postRequest = call.receive<CreatePostRequest>()
        val result = postUserRepository.createPost(postRequest)
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
                httpStatusCode, WebResponse<List<Int>>(
                    message = result.exceptionOrNull()?.cause?.localizedMessage ?: "Unknown error occurred",
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