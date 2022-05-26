package id.sosialpedia.chats.rooms

import id.sosialpedia.chats.messages.domain.MessageDataSource
import id.sosialpedia.chats.messages.domain.model.Message
import id.sosialpedia.chats.rooms.domain.RoomsDataSource
import id.sosialpedia.chats.routes.model.CreateMessageRequest
import id.sosialpedia.chats.util.UserAlreadyExists
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
    private val messageDataSource: MessageDataSource,
    private val roomsDataSource: RoomsDataSource
) {
    private val members = ConcurrentHashMap<String /* userId */, WebSocketSession>()

    suspend fun onJoin(
        userId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(userId)) {
            throw UserAlreadyExists(userId)
        }
        members[userId] = socket
        socket.send(Frame.Text("there are ${members.size} online members"))
    }

    suspend fun createRoom(userId: String) {
        val roomId = UUID.randomUUID().toShuffledMD5(25)
        if (roomsDataSource.createRoom(roomId, userId) <= 0) {
            throw IllegalArgumentException("can't create room")
        }
    }

    suspend fun joinRoom(roomId: String, userId: String) {
        if (roomsDataSource.joinRoom(roomId, userId) <= 0) {
            throw IllegalArgumentException("can't join room")
        }
    }

    suspend fun sendMessage(createMessageRequest: CreateMessageRequest) {

        val roomWithParticipants = roomsDataSource.getRoomWithParticipants(createMessageRequest.roomId)
            ?: throw IllegalArgumentException("participants not found")
        val messageEntity = Message(
            id = UUID.randomUUID().toShuffledMD5(20),
            roomId = createMessageRequest.roomId,
            userId = createMessageRequest.userId,
            text = createMessageRequest.text,
            createdAt = System.currentTimeMillis(),
            isRead = false,
            isReceived = false
        )
        val insertedValues = messageDataSource.insertMessage(
            messageEntity
        )
        if (insertedValues > 0) {
            roomWithParticipants.usersId.forEach { userId ->
                val parsedMessage = Json.encodeToString(messageEntity)
                members[userId]?.send(parsedMessage)
            }
        }
    }

    suspend fun getAllMessagesFrom(roomId: String): List<Message> {
        return messageDataSource.getAllMessagesFrom(roomId)
    }

    suspend fun tryDisconnect(userId: String) {
        members[userId]?.close()
        if (members.containsKey(userId)) {
            members.remove(userId)
        }
        println("members count: ${members.keys().toList().size}")
    }
}