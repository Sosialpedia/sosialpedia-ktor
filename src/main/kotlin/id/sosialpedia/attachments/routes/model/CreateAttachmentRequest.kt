package id.sosialpedia.attachments.routes.model

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */
@kotlinx.serialization.Serializable
data class CreateAttachmentRequest(
    val postId: String,
    val attachmentUrl: String,
    val attachmentType: String,
)
