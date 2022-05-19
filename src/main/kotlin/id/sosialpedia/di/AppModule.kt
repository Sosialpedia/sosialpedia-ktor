package id.sosialpedia.di

import id.sosialpedia.attachments.data.AttachmentRepositoryImpl
import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.chatwebsocket.data.MessageDataSourceImpl
import id.sosialpedia.chatwebsocket.domain.MessageDataSource
import id.sosialpedia.chatwebsocket.domain.RoomController
import id.sosialpedia.comments.data.CommentRepositoryImpl
import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.posts.data.PostRepositoryImpl
import id.sosialpedia.posts.domain.PostRepository
import id.sosialpedia.users.data.UserRepositoryImpl
import id.sosialpedia.users.domain.UserRepository
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val mainModule = module {
    single {
        Database.connect(
            "jdbc:postgresql://localhost:5432/my_postgres", driver = "org.postgresql.Driver",
            user = "root", password = "root"
        )
    }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<PostRepository> { PostRepositoryImpl(get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<AttachmentRepository> { AttachmentRepositoryImpl(get()) }
    single<MessageDataSource> { MessageDataSourceImpl() }
    single { RoomController(get()) }
}