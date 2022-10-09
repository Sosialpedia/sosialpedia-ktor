package id.sosialpedia.chats.rooms.data.model

import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Table

/**
 * @author Samuel Mareno
 * @Date 25/05/22
 */
object ParticipantsEntity : Table("participants") {
    val roomId = varchar("room_id", 25).references(RoomsEntity.id)
    val userId = varchar("user_id", 16).references(UserEntity.id)
}