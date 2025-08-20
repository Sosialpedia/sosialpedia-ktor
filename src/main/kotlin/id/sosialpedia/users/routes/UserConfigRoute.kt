package id.sosialpedia.users.routes

import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
fun Route.userConfig() {

    val userRepository by inject<UserRepository>(UserRepository::class.java)

    get("/") {
        call.respond("Hello Sosialpedia!")
    }

    authenticate {
        get("/users") {
            var httpStatusCode = HttpStatusCode.OK
            val result = userRepository.getAllUsers()
            if (result.isSuccess) {
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        httpStatusCode.description,
                        result.getOrNull(),
                        httpStatusCode.value
                    )
                )
            } else {
                httpStatusCode = HttpStatusCode.NotAcceptable
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        httpStatusCode.description,
                        result.exceptionOrNull()?.cause?.localizedMessage,
                        httpStatusCode.value
                    )
                )
            }
        }
        get("/user") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class) ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                return@get
            }
            var httpStatusCode = HttpStatusCode.OK
            val resultUser = userRepository.getUserById(userId)
            val user = resultUser.getOrNull() ?: kotlin.run {
                httpStatusCode = HttpStatusCode.NotFound
                call.respond(
                    httpStatusCode,
                    WebResponse(
                        httpStatusCode.description,
                        "User not found",
                        httpStatusCode.value
                    )
                )
                return@get
            }
            call.respond(
                httpStatusCode,
                WebResponse(
                    httpStatusCode.description,
                    listOf(user),
                    httpStatusCode.value
                )
            )
        }

        put("user/upd4t3l0g1n") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class) ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                return@put
            }
            val result = userRepository.updateUserLogin(
                id = userId,
                ipAddress = "1.0.1.0.1",
                device = "MacOS Intellij IDEA"
            )
            call.respond("operation: ${result.isSuccess}")
        }

        delete("user/{username}") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class) ?: kotlin.run {
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                return@delete
            }
            val user = userRepository.getUserById(userId).getOrNull() ?: kotlin.run {
                call.respond(HttpStatusCode.NotFound, "User not found")
                return@delete
            }
            if (user.username != call.parameters["username"]) {
                call.respond(HttpStatusCode.Forbidden, "You can't delete other user")
                return@delete
            }
            userRepository.deleteUser(user.id)
            call.respond("User deleted")
        }
    }
}