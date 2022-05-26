package id.sosialpedia.chats.rooms.domain

import id.sosialpedia.chats.rooms.domain.model.Room
import id.sosialpedia.chats.rooms.domain.model.RoomWithMembers

/**
 * @author Samuel Mareno
 * @Date 25/05/22
 */
interface RoomsDataSource {

    suspend fun createRoom(roomId: String, userId: String): Int

    suspend fun checkRoom(roomId: String): Room?

    suspend fun joinRoom(roomId: String, userId: String): Int

    suspend fun deleteRoom(roomId: String)

    suspend fun getRoomWithParticipants(roomId: String): RoomWithMembers?
}