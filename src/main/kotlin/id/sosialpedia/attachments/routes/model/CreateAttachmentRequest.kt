package id.sosialpedia.attachments.routes.model

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */
@kotlinx.serialization.Serializable
data class CreateAttachmentRequest(
    val linkUrl: String,
    val type: String,
    val postId: String?,
    val commentId: String?,
    val childCommentId: String?
)
