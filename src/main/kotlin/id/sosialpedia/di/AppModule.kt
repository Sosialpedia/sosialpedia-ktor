package id.sosialpedia.di

import id.sosialpedia.attachments.data.AttachmentRepositoryImpl
import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.chats.messages.data.MessageDataSourceImpl
import id.sosialpedia.chats.messages.domain.MessageDataSource
import id.sosialpedia.chats.rooms.RoomController
import id.sosialpedia.chats.rooms.data.RoomsDataSourceImpl
import id.sosialpedia.chats.rooms.domain.RoomsDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

fun mainModule(environment: ApplicationEnvironment): org.koin.core.module.Module {
    return module {
        single {
            val username = environment.config.property("db.username").getString()
            val password = environment.config.property("db.password").getString()
            Database.connect(
                "jdbc:postgresql://localhost:5432/my_postgres", driver = "org.postgresql.Driver",
                user = username, password = password
            )
        }
        single<AttachmentRepository> { AttachmentRepositoryImpl(get()) }
        single<MessageDataSource> { MessageDataSourceImpl(get()) }
        single<RoomsDataSource> { RoomsDataSourceImpl(get()) }
        single { RoomController(get(), get()) }
    }
}