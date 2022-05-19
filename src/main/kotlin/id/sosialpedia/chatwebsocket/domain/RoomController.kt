package id.sosialpedia.chatwebsocket.domain

import id.sosialpedia.chatwebsocket.domain.model.Member
import id.sosialpedia.chatwebsocket.domain.model.Message
import id.sosialpedia.chatwebsocket.domain.model.Room
import id.sosialpedia.chatwebsocket.routes.model.CreateMessageRequest
import id.sosialpedia.util.toShuffledMD5
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
class RoomController(
    private val messageDataSource: MessageDataSource
) {

    private val rooms = ConcurrentHashMap<String, Room>()

    fun onJoin(
        username: String,
        roomId: String,
        socket: WebSocketSession
    ) {

        if (!rooms.containsKey(roomId)) {
            val newRoom = messageDataSource.createNewRoom(roomId)
            rooms[roomId] = newRoom
        }
        val roomMembers = rooms[roomId]?.members
        val memberExistOnRoom = roomMembers?.containsKey(username) == true
        if (memberExistOnRoom) {
            throw IllegalAccessException("username: $username is already exists")
        }
        roomMembers?.set(
            username, Member(username, socket)
        )
    }

    suspend fun sendMessage(createMessageRequest: CreateMessageRequest) {

        val messageEntity = Message(
            id = UUID.randomUUID().toShuffledMD5(20),
            roomId = createMessageRequest.roomId,
            username = createMessageRequest.username,
            text = createMessageRequest.text,
            timestamp = System.currentTimeMillis(),
            isRead = false,
            isReceived = false
        )
        messageDataSource.insertMessage(
            messageEntity
        )
        rooms[createMessageRequest.roomId]?.members?.values?.forEach { member ->
            val parsedMessage = Json.encodeToString(messageEntity)
            member.socket.send(parsedMessage)
        }
    }

    suspend fun getAllMessagesFrom(roomId: String): List<Message> {
        return messageDataSource.getAllMessagesFrom(roomId)
    }

    suspend fun tryDisconnect(roomId: String, username: String) {
        val members = rooms[roomId]?.members
        members?.get("username")?.socket?.close()
        if (members?.containsKey(username) == true) {
            members.remove(username)
        }
        if (members?.isEmpty() == true) {
            rooms.remove(roomId)
        }
        println("removing roomId: $roomId, username: $username")
        println("status roomId: $roomId, member count: ${members?.size}")
    }
}