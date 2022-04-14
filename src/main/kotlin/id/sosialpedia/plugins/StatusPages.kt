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
            val httpStatusCode = HttpStatusCode.MethodNotAllowed
            call.respond(
                httpStatusCode, WebResponse<List<String>>(
                    code = httpStatusCode.value,
                    data = emptyList(),
                    message = cause.cause?.localizedMessage ?: "Unknown error occurred"
                )
            )
        }
    }
}