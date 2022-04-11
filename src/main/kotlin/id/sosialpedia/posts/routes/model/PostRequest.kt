package id.sosialpedia.posts.routes.model

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PostRequest(
    val userId: String,
    val content: String,
    val haveAttachment: Boolean,
)
