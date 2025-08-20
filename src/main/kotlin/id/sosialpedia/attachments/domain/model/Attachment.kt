package id.sosialpedia.attachments.domain.model

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */

@kotlinx.serialization.Serializable
data class Attachment(
    val id: String,
    val postId: String,
    val attachmentUrl: String,
    val attachmentType: String,
    val createdAt: Long
)
