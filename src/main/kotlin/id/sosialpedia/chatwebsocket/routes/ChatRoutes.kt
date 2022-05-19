package id.sosialpedia.chatwebsocket.routes

import id.sosialpedia.chatwebsocket.domain.RoomController
import id.sosialpedia.chatwebsocket.routes.model.CreateMessageRequest
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.ktor.ext.inject
import java.time.Duration

/**
 * @author Samuel Mareno
 * @Date 20/04/22
 */
fun Application.configureChats() {

    val roomController by inject<RoomController>()

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/chat") {

            val username = call.parameters["username"] ?: throw IllegalArgumentException("username can't be empty")
            val roomId = call.parameters["roomId"] ?: throw IllegalArgumentException("room Id is empty")
            println("Adding user! with username: $username in roomId: $roomId")
            try {
                roomController.onJoin(username, roomId, this)
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        roomController.sendMessage(
                            createMessageRequest = CreateMessageRequest(
                                roomId = roomId,
                                username = username,
                                text = frame.readText()
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                roomController.tryDisconnect(
                    roomId = roomId,
                    username = username
                )
            }
        }
        get("/chat/messages/{roomId}") {
            val roomId = call.parameters["roomId"] ?: throw IllegalArgumentException("Room Id is empty")
            call.respond(
                roomController.getAllMessagesFrom(roomId)
            )
        }
    }


}