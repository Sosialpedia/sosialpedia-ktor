package id.sosialpedia.plugins

import id.sosialpedia.attachments.routes.configureAttachmentRoutes
import id.sosialpedia.chats.routes.configureChats
import id.sosialpedia.comments.routes.commentConfig
import id.sosialpedia.posts.routes.postConfig
import id.sosialpedia.security.hashing.HashingService
import id.sosialpedia.security.token.TokenConfig
import id.sosialpedia.security.token.TokenService
import id.sosialpedia.users.routes.userConfig
import id.sosialpedia.users.routes.userLogin
import id.sosialpedia.users.routes.userRegister
import id.sosialpedia.votes.routes.configureVotesRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig,
) {
    routing {
        userRegister(hashingService)
        userLogin(hashingService, tokenService, tokenConfig)
        userConfig()
        postConfig()
        commentConfig()
        configureVotesRoutes()
        configureAttachmentRoutes()
        configureChats()
    }
}
