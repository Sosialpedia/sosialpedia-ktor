package id.sosialpedia.posts.routes.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val userId: String,
    val content: String,
    val haveAttachment: Boolean,
)
