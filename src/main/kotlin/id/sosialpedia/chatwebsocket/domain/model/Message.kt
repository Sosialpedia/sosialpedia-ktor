package id.sosialpedia.chatwebsocket.domain.model

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
@kotlinx.serialization.Serializable
data class Message(
    val id: String,
    val roomId: String,
    val username: String,
    val text: String,
    val isRead: Boolean,
    val isReceived: Boolean,
    val timestamp: Long
)
