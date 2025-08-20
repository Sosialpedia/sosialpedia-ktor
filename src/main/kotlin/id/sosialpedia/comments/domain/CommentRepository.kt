package id.sosialpedia.comments.domain

import id.sosialpedia.comments.domain.model.ChildComment
import id.sosialpedia.comments.domain.model.Comment
import id.sosialpedia.comments.routes.model.ChildCommentRequest
import id.sosialpedia.comments.routes.model.CommentRequest

interface CommentRepository {

    suspend fun getCommentsFromPost(postId: String): Result<List<Comment>>
    suspend fun addComment(commentRequest: CommentRequest): Result<Comment>

    suspend fun deleteComment(commentId: String, postId: String, userId: String): Result<String>

    suspend fun getChildComments(commentId: String): Result<List<ChildComment>>

    suspend fun addChildComment(childCommentRequest: ChildCommentRequest): Result<ChildComment>

    suspend fun deleteChildComment(childCommentId: String, commentId: String, userId: String): Result<String>
}