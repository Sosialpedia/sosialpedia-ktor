package id.sosialpedia.posts.di

import id.sosialpedia.posts.data.PostRepositoryImpl
import id.sosialpedia.posts.domain.PostRepository
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 26/05/22
 */
val postsModule = module {
    single<PostRepository> { PostRepositoryImpl(get()) }
}