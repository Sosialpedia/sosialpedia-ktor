package id.sosialpedia.plugins

import id.sosialpedia.attachments.routes.configureAttachmentRoutes
import id.sosialpedia.chats.routes.configureChats
import id.sosialpedia.comments.routes.configureCommentsRouting
import id.sosialpedia.posts.routes.configurePostsRouting
import id.sosialpedia.reaction.routes.configureReactionRoutes
import id.sosialpedia.users.routes.configureUsersRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        configureUsersRouting()
        configurePostsRouting()
        configureCommentsRouting()
        configureReactionRoutes()
        configureAttachmentRoutes()
        configureChats()
    }
}
