package id.sosialpedia.chats.routes

import id.sosialpedia.chats.rooms.RoomController
import id.sosialpedia.chats.rooms.domain.RoomsDataSource
import id.sosialpedia.chats.routes.model.CreateMessageRequest
import id.sosialpedia.chats.util.UserNotInRoom
import id.sosialpedia.users.domain.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

/**
 * @author Samuel Mareno
 * @Date 20/04/22
 */
fun Route.configureChats() {

    val roomController by inject<RoomController>()
    val roomsDataSource by inject<RoomsDataSource>()
    val userRepository by inject<UserRepository>()
    webSocket("/chat") {

        val userId = call.parameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        println("Adding user! with userId: $userId and socket: $this")

        userRepository.getUserById(userId) ?: throw IllegalArgumentException("userId isn't found")

        try {
            roomController.onJoin(userId, this)
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val createMessageRequest = Json.decodeFromString<CreateMessageRequest>(frame.readText())
                    roomController.sendMessage(createMessageRequest)
                }
            }
        } catch (e: UserNotInRoom) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDisconnect(userId)
        }
    }
    get("/chat/messages") {
        val roomId = call.parameters["roomId"] ?: throw IllegalArgumentException("Room Id is empty")
        call.respond(
            roomController.getAllMessagesFrom(roomId)
        )
    }
    get("/chat/rooms") {
        val roomId = call.request.queryParameters["roomId"] ?: throw IllegalArgumentException("Room Id is empty")
        val roomWithMembers = roomsDataSource.getRoomWithParticipants(roomId) ?: "empty"
        call.respond(HttpStatusCode.OK, roomWithMembers)
    }


}