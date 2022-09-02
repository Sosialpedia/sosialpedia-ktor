package id.sosialpedia.users.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * @author Samuel Mareno
 * @Date 02/09/22
 */

fun Route.authenticateUser() {
    authenticate {
        get("user/authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("user/secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            val username = principal?.getClaim("username", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId and username is $username")
        }
    }
}