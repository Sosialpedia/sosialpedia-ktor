package id.sosialpedia.chatwebsocket.data

import id.sosialpedia.chatwebsocket.domain.MessageDataSource
import id.sosialpedia.chatwebsocket.domain.model.Member
import id.sosialpedia.chatwebsocket.domain.model.Message
import id.sosialpedia.chatwebsocket.domain.model.Room
import id.sosialpedia.util.toShuffledMD5
import java.util.UUID

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
class MessageDataSourceImpl : MessageDataSource {

    private val messages = mutableListOf<Message>()

    override suspend fun getAllMessagesFrom(roomId: String): List<Message> {
        return messages.filter { it.roomId == roomId }
    }

    override suspend fun insertMessage(message: Message) {
        messages.add(message)
    }

    override fun createNewRoom(roomId: String): Room {
        return Room(
            id = roomId,
            createdAt = System.currentTimeMillis()
        )
    }
}