package id.sosialpedia.posts.domain.model


@kotlinx.serialization.Serializable
data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val haveAttachment: Boolean,
    val createdAt: Long,
    val totalLike: Int = 0,
    val totalDislike: Int = 0,
    val totalComment: Int = 0,
)
