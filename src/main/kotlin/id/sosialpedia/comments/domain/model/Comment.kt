package id.sosialpedia.comments.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: String,
    val postId: String,
    val userId: String,
    val username: String,
    val userProfilePicUrl: String?,
    val content: String,
    val parentCommentId: String?,
    val haveAttachment: Boolean,
    val createdAt: Long,
    val totalLikes: Long = 0,
    val totalDislikes: Long = 0,
    val replies: List<Comment> = emptyList()
)
