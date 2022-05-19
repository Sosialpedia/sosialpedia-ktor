package id.sosialpedia.plugins

import id.sosialpedia.chatwebsocket.helper.ChatSession
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.sessions.*
import kotlin.collections.set
import kotlin.random.Random

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<ChatSession>("CHAT_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    intercept(Plugins) {
        if (call.sessions.get<ChatSession>() == null) {
            val username = call.parameters["username"] ?: "guest ${Random.nextInt(3)}"
            call.sessions.set(ChatSession(username, generateSessionId()))
        }
    }
}
