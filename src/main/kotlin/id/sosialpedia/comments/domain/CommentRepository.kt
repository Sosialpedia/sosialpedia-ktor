package id.sosialpedia.comments.domain

import id.sosialpedia.comments.domain.model.Comment
import id.sosialpedia.comments.routes.model.CommentRequest

interface CommentRepository {

    suspend fun getCommentsFromPost(postId: String): Result<List<Comment>>

    suspend fun addComment(commentRequest: CommentRequest): Result<Comment>

    suspend fun deleteCommentToPost(postId: String, userId: String)

    suspend fun deleteCommentToComment(commentId: String, userId: String)

    suspend fun testingTransaction(): Result<List<String>>
}