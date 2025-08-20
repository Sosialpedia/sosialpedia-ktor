package id.sosialpedia

import id.sosialpedia.comments.di.commentsModule
import id.sosialpedia.core.di.coreModule
import id.sosialpedia.di.mainModule
import id.sosialpedia.plugins.*
import id.sosialpedia.posts.di.postsModule
import id.sosialpedia.security.hashing.SHA256HashingService
import id.sosialpedia.security.token.JwtTokenService
import id.sosialpedia.security.token.TokenConfig
import id.sosialpedia.users.di.usersModule
import id.sosialpedia.votes.di.voteModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
        modules(
            voteModule,
            mainModule,
            usersModule,
            postsModule,
            commentsModule,
            coreModule
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

    configureSecurity(config = tokenConfig)
    configureSockets()
    configureRouting(hashingService, tokenService, tokenConfig)
    configureStatusPages()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
