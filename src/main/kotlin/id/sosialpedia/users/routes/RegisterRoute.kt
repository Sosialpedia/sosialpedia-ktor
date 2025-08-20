package id.sosialpedia.users.routes

import id.sosialpedia.security.hashing.HashingService
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent

/**
 * @author Samuel Mareno
 * @Date 02/09/22
 */

fun Route.userRegister(
    hashingService: HashingService
) {

    val userRepository by KoinJavaComponent.inject<UserRepository>(UserRepository::class.java)

    post("user/signup") {
        var httpStatusCode = HttpStatusCode.Created
        val createUserRequest = call.receiveNullable<CreateUserRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isPwTooShort = createUserRequest.password.length < 8
        if (isPwTooShort) {
            httpStatusCode = HttpStatusCode.NotAcceptable
            call.respond(
                httpStatusCode,
                WebResponse(
                    message = httpStatusCode.description,
                    data = listOf("Password is too short"),
                    code = httpStatusCode.value
                )
            )
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(createUserRequest.password)
        val newUserRequest = createUserRequest.copy(
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val result = userRepository.registerUser(newUserRequest)
        if (result.isSuccess) {
            call.respond(
                status = httpStatusCode,
                message = WebResponse(
                    message = httpStatusCode.description,
                    data = result.getOrNull(),
                    code = httpStatusCode.value
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
}