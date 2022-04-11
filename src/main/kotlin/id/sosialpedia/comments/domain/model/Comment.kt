package id.sosialpedia.comments.domain.model

@kotlinx.serialization.Serializable
data class Comment(
    val id: String,
    val userId: String,
    val content: String,
    val postId: String,
    val commentId: String?,
    val haveAttachment: Boolean,
    val createdAt: String,
    val totalLike: Int = 0,
    val totalDislike: Int = 0,
    val totalComment: Int = 0
)
