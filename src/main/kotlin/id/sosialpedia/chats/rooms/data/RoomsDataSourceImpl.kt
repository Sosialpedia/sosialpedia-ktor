package id.sosialpedia.chats.rooms.data

import id.sosialpedia.chats.rooms.data.model.ParticipantsEntity
import id.sosialpedia.chats.rooms.data.model.RoomsEntity
import id.sosialpedia.chats.rooms.domain.RoomsDataSource
import id.sosialpedia.chats.rooms.domain.model.Room
import id.sosialpedia.chats.rooms.domain.model.RoomWithMembers
import id.sosialpedia.util.execAndMap
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 25/05/22
 */
class RoomsDataSourceImpl(
    private val database: Database
) : RoomsDataSource {

    override suspend fun createRoom(roomId: String, userId: String): Int {
        return newSuspendedTransaction(db = database) {
            try {
                RoomsEntity.insert {
                    it[id] = roomId
                    it[author] = UUID.fromString(userId)
                    it[createdAt] = System.currentTimeMillis()
                }.insertedCount
            } catch (e: Exception) {
                println("error create room: $e")
                0
            }
        }
    }

    override suspend fun checkRoom(roomId: String): Room? {
        return newSuspendedTransaction {
            try {
                RoomsEntity.select(RoomsEntity.id eq roomId)
                    .map {
                        Room(
                            id = it[RoomsEntity.id],
                            createdAt = it[RoomsEntity.createdAt],
                        )
                    }.firstOrNull()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // create table participants on PostgreSQL
    override suspend fun joinRoom(roomId: String, userId: String): Int {
        return newSuspendedTransaction {
            try {
                ParticipantsEntity.insert {
                    it[ParticipantsEntity.roomId] = roomId
                    it[ParticipantsEntity.userId] = UUID.fromString(userId)
                }.insertedCount
            } catch (e: Exception) {
                println("create participants error: $e")
                0
            }
        }
    }

    override suspend fun deleteRoom(roomId: String) {
        newSuspendedTransaction {
            RoomsEntity.deleteWhere { RoomsEntity.id eq roomId }
        }
    }

    override suspend fun getRoomWithParticipants(roomId: String): RoomWithMembers {
        val query = """
            SELECT rooms.id AS "rooms.id", users.id AS "users.id", users.username AS "users.username" FROM rooms
            JOIN participants ON (participants.room_id = rooms.id)
            JOIN users ON (users.id = participants.user_id) WHERE (rooms.id = '$roomId')
        """.trimIndent()

        val roomWithMembers = RoomWithMembers(roomId)
        newSuspendedTransaction {
            query.execAndMap { resultSet ->
                roomWithMembers.usersId.add(resultSet.getString("users.id"))
                roomWithMembers.usernames.add(resultSet.getString("users.username"))
            }
        }
        return roomWithMembers
    }
}