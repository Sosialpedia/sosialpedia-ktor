package id.sosialpedia.users.routes

import id.sosialpedia.users.data.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureUsersRouting() {
    val userRepository by inject<UserRepository>()
    routing {
        get("/users") {
            val result = userRepository.getAllUsers()
            call.respond(result)
        }
        post("users/register") {
            val result = userRepository.registerUser()
            if (result.isSuccess) {
                call.respond(HttpStatusCode.Created, "success")
            } else {
                call.respond(HttpStatusCode.OK, "${result.exceptionOrNull()?.cause?.localizedMessage}")
            }
        }
        put("users/upd4t3l0g1n/{userId}") {
            val userId = call.parameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
            val result = userRepository.updateUserLogin(
                id = userId,
                ipAddress = "1.0.1.0.1",
                device = "MacOS Intellij IDEA"
            )
            call.respond("operation: ${result.isSuccess}")
        }
        delete("users/{userId}") {
            call.parameters["userId"]?.let {
                userRepository.deleteUser(it)
                call.respond("User deleted")
            }
        }
    }
}