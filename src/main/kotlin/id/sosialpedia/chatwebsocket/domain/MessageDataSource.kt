package id.sosialpedia.chatwebsocket.domain

import id.sosialpedia.chatwebsocket.domain.model.Member
import id.sosialpedia.chatwebsocket.domain.model.Message
import id.sosialpedia.chatwebsocket.domain.model.Room

interface MessageDataSource {


    suspend fun getAllMessagesFrom(roomId: String): List<Message>

    suspend fun insertMessage(message: Message)

    fun createNewRoom(roomId: String): Room
}