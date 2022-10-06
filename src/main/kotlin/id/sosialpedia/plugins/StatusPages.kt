package id.sosialpedia.plugins

import id.sosialpedia.util.WebResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.sql.SQLException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<SQLException> { call, cause ->
            cause.printStackTrace()
            val httpStatusCode = HttpStatusCode.MethodNotAllowed
            call.respond(
                httpStatusCode, WebResponse<List<String>>(
                    code = httpStatusCode.value,
                    data = emptyList(),
                    message = "Unknown error occurred"
                )
            )
        }
        exception<NoSuchElementException> { call, cause ->
            val httpStatusCode = HttpStatusCode.BadRequest
            call.respond(
                httpStatusCode, WebResponse<List<String>>(
                    code = httpStatusCode.value,
                    data = emptyList(),
                    message = cause.message ?: "Unknown error occurred"
                )
            )
        }
    }
}