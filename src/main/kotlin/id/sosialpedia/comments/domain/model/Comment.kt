package id.sosialpedia.comments.domain.model

@kotlinx.serialization.Serializable
data class Comment(
    val id: String,
    val userId: String,
    val content: String,
    val postId: String,
    val haveAttachment: Boolean,
    val createdAt: Long,
    val totalLike: Long = 0,
    val totalDislike: Long = 0,
    val totalChildComment: Long = 0
)
