package id.sosialpedia.posts.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val haveAttachment: Boolean,
    val createdAt: String,
    val totalLike: Int = 0,
    val totalDislike: Int = 0,
    val totalComment: Int = 0,
)
