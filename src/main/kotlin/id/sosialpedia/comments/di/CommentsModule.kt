package id.sosialpedia.comments.di

import id.sosialpedia.comments.data.CommentRepositoryImpl
import id.sosialpedia.comments.domain.CommentRepository
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 26/05/22
 */

val commentsModule = module {
    single<CommentRepository> { CommentRepositoryImpl(get()) }
}