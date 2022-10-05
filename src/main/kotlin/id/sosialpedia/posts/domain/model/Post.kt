package id.sosialpedia.posts.domain.model

import id.sosialpedia.core.domain.Owner


@kotlinx.serialization.Serializable
data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val haveAttachment: Boolean,
    val createdAt: Long,
    val owner: Owner,
    val totalLike: Int,
    val totalDislike: Int,
    val totalComment: Int,
)
