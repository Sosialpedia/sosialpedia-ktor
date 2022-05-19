package id.sosialpedia

import id.sosialpedia.attachments.routes.configureAttachmentRoutes
import id.sosialpedia.chatwebsocket.routes.configureChats
import id.sosialpedia.comments.routes.configureCommentsRouting
import id.sosialpedia.di.mainModule
import id.sosialpedia.plugins.*
import id.sosialpedia.posts.routes.configurePostsRouting
import id.sosialpedia.reaction.di.reactionModule
import id.sosialpedia.reaction.routes.configureReactionRoutes
import id.sosialpedia.users.routes.configureUsersRouting
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Koin) {
        modules(mainModule, reactionModule)
    }

    configureRouting()
    configureUsersRouting()
    configurePostsRouting()
    configureCommentsRouting()
    configureReactionRoutes()
    configureAttachmentRoutes()
    configureChats()
    configureStatusPages()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
}
