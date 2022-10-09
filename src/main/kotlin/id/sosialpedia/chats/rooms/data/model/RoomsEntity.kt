package id.sosialpedia.chats.rooms.data.model

import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Table

/**
 * @author Samuel Mareno
 * @Date 24/05/22
 */
object RoomsEntity : Table("rooms") {
    val id = varchar("id", 25)
    val author = varchar("author", 16).references(UserEntity.id)
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}