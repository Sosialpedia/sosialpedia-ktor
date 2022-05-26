package id.sosialpedia.chats.messages.data.model

import id.sosialpedia.chats.rooms.data.model.RoomsEntity
import id.sosialpedia.users.data.model.UsersEntity
import org.jetbrains.exposed.sql.Table

/**
 * @author Samuel Mareno
 * @Date 24/05/22
 */
object MessagesEntity : Table("messages") {
    val id = varchar("id", 32)
    val userId = varchar("user_id", 16).references(UsersEntity.id)
    val roomId = varchar("room_id", 25).references(RoomsEntity.id)
    val text = text("text")
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id, userId, roomId)
}