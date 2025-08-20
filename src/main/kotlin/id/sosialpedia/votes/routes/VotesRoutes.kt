package id.sosialpedia.votes.routes

import id.sosialpedia.util.WebResponse
import id.sosialpedia.votes.domain.VoteRepository
import id.sosialpedia.votes.domain.model.VoteRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Configures the refactored routes for handling votes.
 * This replaces the old separate endpoints for like/dislike/remove
 * with a single, intelligent endpoint for posts and one for comments.
 */
fun Route.configureVotesRoutes() {
    // Inject the VoteRepository using Koin
    val voteRepository by inject<VoteRepository>()

    route("/votes") {
        /**
         * Handles voting on a post.
         * This single endpoint manages liking, disliking, and removing votes.
         * The action is determined by the logic within the repository.
         */
        post("/post/{postId}") {
            val postId = call.parameters["postId"]
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing postId parameter")

            try {
                val request = call.receive<VoteRequest>()
                val result = voteRepository.addOrUpdateVote(
                    userId = request.userId,
                    postId = postId,
                    commentId = null,
                    voteType = request.voteType
                )

                if (result.isSuccess) {
                    call.respond(
                        HttpStatusCode.OK, WebResponse(
                            message = "Vote processed successfully",
                            data = result.getOrNull(),
                            code = HttpStatusCode.OK.value
                        )
                    )
                } else {
                    throw result.exceptionOrNull() ?: Exception("Unknown error")
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest, WebResponse(
                        message = e.localizedMessage ?: "Invalid request",
                        data = null,
                        code = HttpStatusCode.BadRequest.value
                    )
                )
            }
        }

        /**
         * Handles voting on a comment.
         * This single endpoint manages liking, disliking, and removing votes.
         */
        post("/comment/{commentId}") {
            val commentId = call.parameters["commentId"]
                ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing commentId parameter")

            try {
                val request = call.receive<VoteRequest>()
                val result = voteRepository.addOrUpdateVote(
                    userId = request.userId,
                    postId = null,
                    commentId = commentId,
                    voteType = request.voteType
                )

                if (result.isSuccess) {
                    call.respond(
                        HttpStatusCode.OK, WebResponse(
                            message = "Vote processed successfully",
                            data = result.getOrNull(),
                            code = HttpStatusCode.OK.value
                        )
                    )
                } else {
                    throw result.exceptionOrNull() ?: Exception("Unknown error")
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest, WebResponse(
                        message = e.localizedMessage ?: "Invalid request",
                        data = null,
                        code = HttpStatusCode.BadRequest.value
                    )
                )
            }
        }
    }
}
