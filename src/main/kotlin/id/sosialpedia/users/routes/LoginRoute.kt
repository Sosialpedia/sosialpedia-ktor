package id.sosialpedia.users.routes

import id.sosialpedia.security.hashing.HashingService
import id.sosialpedia.security.hashing.SaltedHash
import id.sosialpedia.security.token.TokenClaim
import id.sosialpedia.security.token.TokenConfig
import id.sosialpedia.security.token.TokenService
import id.sosialpedia.users.data.model.AuthResponse
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.routes.model.AuthRequest
import id.sosialpedia.util.WebResponse
import io.ktor.http.*
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
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
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

        val userResult = if (request.username.contains("@") && request.username.contains(".")) {
            userRepository.getUserByEmail(request.username)
        } else {
            userRepository.getUserByUsername(request.username)
        }
        val user = userResult.getOrNull() ?: kotlin.run {
            httpStatusCode = HttpStatusCode.BadRequest
            call.respond(
                httpStatusCode,
                WebResponse(
                    httpStatusCode.description,
                    "Username or password is invalid",
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
            httpStatusCode = HttpStatusCode.BadRequest
            call.respond(
                httpStatusCode,
                WebResponse(
                    httpStatusCode.description,
                    "Username or password is invalid",
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
            call.respond(
                httpStatusCode, WebResponse(
                    httpStatusCode.description,
                    AuthResponse(token),
                    httpStatusCode.value
                )
            )
        }
    }
}