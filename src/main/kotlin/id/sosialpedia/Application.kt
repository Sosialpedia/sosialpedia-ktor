package id.sosialpedia

import id.sosialpedia.comments.di.commentsModule
import id.sosialpedia.di.mainModule
import id.sosialpedia.plugins.*
import id.sosialpedia.posts.di.postsModule
import id.sosialpedia.reaction.di.reactionModule
import id.sosialpedia.users.di.usersModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Koin) {
        modules(
            reactionModule,
            mainModule(this@module.environment),
            usersModule,
            postsModule,
            commentsModule
        )
    }

    configureSockets()
    configureRouting()
    configureStatusPages()
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
