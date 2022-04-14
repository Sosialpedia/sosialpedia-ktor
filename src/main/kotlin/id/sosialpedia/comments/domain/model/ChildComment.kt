package id.sosialpedia.comments.domain.model

/**
 * @author Samuel Mareno
 * @Date 13/04/22
 */
@kotlinx.serialization.Serializable
data class ChildComment(
    val id: String,
    val userId: String,
    val postId: String,
    val commentId: String,
    val content: String,
    val haveAttachment: Boolean,
    val createdAt: String
)
