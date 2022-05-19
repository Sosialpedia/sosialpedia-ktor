package id.sosialpedia.chatwebsocket.domain.model

import io.ktor.websocket.*

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
data class Member(
    val username: String,
    val socket: WebSocketSession
)
