package id.sosialpedia.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import id.sosialpedia.chats.helper.ChatSession
import id.sosialpedia.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.sessions.*
import kotlin.collections.set
import kotlin.random.Random

fun Application.configureSecurity(config: TokenConfig) {
    install(Sessions) {
        cookie<ChatSession>("CHAT_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    authentication {
        jwt {
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }

    intercept(Plugins) {
        if (call.sessions.get<ChatSession>() == null) {
            val username = call.parameters["username"] ?: "guest ${Random.nextInt(3)}"
            call.sessions.set(ChatSession(username, generateSessionId()))
        }
    }
}
