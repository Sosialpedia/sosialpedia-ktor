package id.sosialpedia.comments.routes.model

/**
 * @author Samuel Mareno
 * @Date 13/04/22
 */

@kotlinx.serialization.Serializable
data class ChildCommentRequest(
    val userId: String,
    val postId: String,
    val commentId: String,
    val content: String,
    val haveAttachment: Boolean
)
