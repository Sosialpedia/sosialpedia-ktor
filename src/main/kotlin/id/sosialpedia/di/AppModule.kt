package id.sosialpedia.di

import id.sosialpedia.comments.data.CommentRepositoryImpl
import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.posts.domain.PostRepository
import id.sosialpedia.posts.data.PostRepositoryImpl
import id.sosialpedia.users.data.UserRepository
import id.sosialpedia.users.data.UserRepositoryImpl
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module

val mainModule = module {
    single {
        Database.connect("jdbc:mysql://localhost:3306/sosialpedia_db", driver = "com.mysql.cj.jdbc.Driver",
            user = "root", password = "jgm4ever")
    }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<PostRepository> {PostRepositoryImpl(get())}
    single<CommentRepository> { CommentRepositoryImpl(get()) }
}