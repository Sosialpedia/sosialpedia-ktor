package id.sosialpedia.attachments.domain.model

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */

@kotlinx.serialization.Serializable
data class Attachment(
    val id: String,
    val linkUrl: String,
    val type: String,
    val postId: String?,
    val commentId: String?,
    val childCommentId: String?
)
