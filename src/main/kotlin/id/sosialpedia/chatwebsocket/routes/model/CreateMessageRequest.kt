package id.sosialpedia.chatwebsocket.routes.model

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
data class CreateMessageRequest(
    val roomId: String,
    val username: String,
    val text: String
)
