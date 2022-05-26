package id.sosialpedia.chats.messages.domain

import id.sosialpedia.chats.messages.domain.model.Message

interface MessageDataSource {


    suspend fun getAllMessagesFrom(roomId: String): List<Message>

    suspend fun insertMessage(message: Message): Int
}