package id.sosialpedia.users.routes

import id.sosialpedia.security.hashing.HashingService
import id.sosialpedia.security.hashing.SaltedHash
import id.sosialpedia.security.token.TokenClaim
import id.sosialpedia.security.token.TokenConfig
import id.sosialpedia.security.token.TokenService
import id.sosialpedia.users.data.AuthResponse
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.routes.model.AuthRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent.inject

/**
 * @author Samuel Mareno
 * @Date 02/09/22
 */

fun Route.userLogin(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    val userRepository by inject<UserRepository>(UserRepository::class.java)

    post("user/signin") {
        var httpStatusCode = HttpStatusCode.OK
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val usernameIsEmpty = request.username.isEmpty()
        val passwordIsShort = request.password.length < 8

        if (usernameIsEmpty || passwordIsShort) {
            httpStatusCode = HttpStatusCode.BadRequest
            call.respond(httpStatusCode)
            return@post
        }

        val user = userRepository.getUserByUsername(request.username) ?: kotlin.run {
            httpStatusCode = HttpStatusCode.BadRequest
            call.respond(
                httpStatusCode,
                WebResponse(
                    httpStatusCode.description,
                    listOf("User is not found"),
                    httpStatusCode.value
                )
            )
            return@post
        }
        val isValidPassword = hashingService.verify(
            password = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            httpStatusCode = HttpStatusCode.Conflict
            call.respond(
                httpStatusCode,
                WebResponse(
                    httpStatusCode.description,
                    listOf("Username or password is invalid"),
                    httpStatusCode.value
                )
            )
            return@post
        } else {
            val token = tokenService.generate(
                config = tokenConfig,
                TokenClaim(
                    name = "userId",
                    value = user.id
                )
            )
            call.respond(httpStatusCode, AuthResponse(token))
        }

    }
}