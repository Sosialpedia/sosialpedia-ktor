package id.sosialpedia.plugins

import id.sosialpedia.users.data.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    routing {
        get("/") {

        }
    }
}
