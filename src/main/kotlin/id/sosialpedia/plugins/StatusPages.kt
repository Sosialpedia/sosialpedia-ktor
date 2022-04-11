package id.sosialpedia.plugins

import id.sosialpedia.util.WebResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import java.sql.SQLException

fun Application.configureStatusPages() {
    routing {
        install(StatusPages) {
            exception<SQLException> { cause ->
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
}