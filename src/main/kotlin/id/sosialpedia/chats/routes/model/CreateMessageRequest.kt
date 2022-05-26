package id.sosialpedia.chats.routes.model

import kotlinx.serialization.Serializable

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
@Serializable
data class CreateMessageRequest(
    val userId: String,
    val roomId: String,
    val text: String
)
