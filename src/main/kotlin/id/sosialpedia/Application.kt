package id.sosialpedia

import id.sosialpedia.comments.di.commentsModule
import id.sosialpedia.di.mainModule
import id.sosialpedia.plugins.*
import id.sosialpedia.posts.di.postsModule
import id.sosialpedia.reaction.di.reactionModule
import id.sosialpedia.security.hashing.SHA256HashingService
import id.sosialpedia.security.token.JwtTokenService
import id.sosialpedia.security.token.TokenConfig
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
            mainModule(),
            usersModule,
            postsModule,
            commentsModule
        )
    }

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    configureSockets()
    configureRouting(hashingService, tokenService, tokenConfig)
    configureStatusPages()
    configureSecurity(config = tokenConfig)
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
