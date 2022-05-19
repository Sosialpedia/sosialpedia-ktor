package id.sosialpedia.chatwebsocket.domain.model

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Samuel Mareno
 * @Date 19/05/22
 */
data class Room(
    val id: String,
    val members: ConcurrentHashMap<String, Member> = ConcurrentHashMap(),
    val createdAt: Long
)
