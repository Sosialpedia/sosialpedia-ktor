package id.sosialpedia.users.routes

import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
fun Route.usersConfig() {

    val userRepository by inject<UserRepository>(UserRepository::class.java)

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
    get("/user/{userId}") {
        var httpStatusCode = HttpStatusCode.OK
        val userId = call.parameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        val result = userRepository.getUserById(userId)
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

    put("user/upd4t3l0g1n/{userId}") {
        val userId = call.parameters["userId"] ?: throw IllegalArgumentException("userId can't be empty")
        val result = userRepository.updateUserLogin(
            id = userId,
            ipAddress = "1.0.1.0.1",
            device = "MacOS Intellij IDEA"
        )
        call.respond("operation: ${result.isSuccess}")
    }
    delete("user/{userId}") {
        call.parameters["userId"]?.let {
            userRepository.deleteUser(it)
            call.respond("User deleted")
        }
    }
}