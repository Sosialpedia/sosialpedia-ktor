package id.sosialpedia

import id.sosialpedia.comments.routes.configureCommentsRouting
import id.sosialpedia.di.mainModule
import id.sosialpedia.plugins.*
import id.sosialpedia.posts.routes.configurePostsRouting
import id.sosialpedia.reaction.di.reactionModule
import id.sosialpedia.reaction.routes.configureReactionRoutes
import id.sosialpedia.users.routes.configureUsersRouting
import id.sosialpedia.util.KoinPlugin
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(KoinPlugin) {
        modules(mainModule, reactionModule)
    }

    configureRouting()
    configureUsersRouting()
    configurePostsRouting()
    configureCommentsRouting()
    configureReactionRoutes()
    configureStatusPages()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
}
