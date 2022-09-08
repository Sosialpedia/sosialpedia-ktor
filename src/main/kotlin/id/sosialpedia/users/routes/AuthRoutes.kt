package id.sosialpedia.users.routes

import id.sosialpedia.users.domain.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * @author Samuel Mareno
 * @Date 02/09/22
 */

fun Route.authenticateUser() {

    val userRepository by inject<UserRepository>()
    authenticate {
        get("user/authenticate") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class) ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }
            userRepository.getUserById(userId) ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized)
                return@get
            }
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("user/secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}