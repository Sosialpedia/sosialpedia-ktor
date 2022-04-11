package id.sosialpedia.comments.routes.model

import kotlinx.serialization.Serializer

@kotlinx.serialization.Serializable
data class CommentRequest(
    val userId: String,
    val content: String,
    val postId: String,
    val commentId: String?,
    val haveAttachment: Boolean,
    val totalLike: Int = 0,
    val totalDislike: Int = 0,
    val totalComment: Int = 0
)
