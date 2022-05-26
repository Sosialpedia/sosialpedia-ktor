package id.sosialpedia.chats.messages.domain.model

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
@kotlinx.serialization.Serializable
data class Message(
    val id: String,
    val roomId: String,
    val userId: String,
    val text: String,
    val isRead: Boolean,
    val isReceived: Boolean,
    val createdAt: Long
)
