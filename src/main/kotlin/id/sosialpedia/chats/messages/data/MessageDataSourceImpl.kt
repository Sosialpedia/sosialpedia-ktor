package id.sosialpedia.chats.messages.data

import id.sosialpedia.chats.messages.data.model.MessagesEntity
import id.sosialpedia.chats.messages.domain.MessageDataSource
import id.sosialpedia.chats.messages.domain.model.Message
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 03/05/22
 */
class MessageDataSourceImpl(
    private val db: Database
) : MessageDataSource {

    override suspend fun getAllMessagesFrom(roomId: String): List<Message> {
        return newSuspendedTransaction(db = db) {
            try {
                MessagesEntity.select(
                    MessagesEntity.roomId eq roomId
                ).map {
                    Message(
                        id = it[MessagesEntity.id],
                        roomId = it[MessagesEntity.roomId],
                        userId = it[MessagesEntity.userId].toString(),
                        text = it[MessagesEntity.text],
                        isRead = false,
                        isReceived = false,
                        createdAt = it[MessagesEntity.createdAt]
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun insertMessage(message: Message): Int {
        return newSuspendedTransaction {
            try {
                MessagesEntity.insert {
                    it[id] = message.id
                    it[userId] = UUID.fromString(message.userId)
                    it[roomId] = message.roomId
                    it[text] = message.text
                    it[createdAt] = message.createdAt
                }.insertedCount
            } catch (e: Exception) {
                println("insert message error: $e")
                0
            }
        }
    }
}