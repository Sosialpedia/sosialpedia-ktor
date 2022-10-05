package id.sosialpedia.di

import id.sosialpedia.attachments.data.AttachmentRepositoryImpl
import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.chats.messages.data.MessageDataSourceImpl
import id.sosialpedia.chats.messages.domain.MessageDataSource
import id.sosialpedia.chats.rooms.RoomController
import id.sosialpedia.chats.rooms.data.RoomsDataSourceImpl
import id.sosialpedia.chats.rooms.domain.RoomsDataSource
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val mainModule = module {
    single {
        val username = System.getenv("DB_USERNAME").toString()
        val password = System.getenv("DB_PASSWORD").toString()
        Database.connect(
            url = "jdbc:postgresql://34.101.248.184:1234/sosialpedia_db",
            driver = "org.postgresql.Driver",
            user = username,
            password = password
        )
    }
    single<AttachmentRepository> { AttachmentRepositoryImpl(get()) }
    single<MessageDataSource> { MessageDataSourceImpl(get()) }
    single<RoomsDataSource> { RoomsDataSourceImpl(get()) }
    single { RoomController(get(), get()) }
}