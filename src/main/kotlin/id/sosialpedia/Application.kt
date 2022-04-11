package id.sosialpedia

import id.sosialpedia.comments.routes.configureCommentsRouting
import id.sosialpedia.plugins.*
import id.sosialpedia.posts.routes.configurePostsRouting
import id.sosialpedia.di.mainModule
import id.sosialpedia.users.routes.configureUsersRouting
import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Koin) {
        modules(mainModule)
    }

    configureRouting()
    configureUsersRouting()
    configurePostsRouting()
    configureCommentsRouting()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
}
