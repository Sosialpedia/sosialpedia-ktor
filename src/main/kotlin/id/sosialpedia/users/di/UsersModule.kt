package id.sosialpedia.users.di

import id.sosialpedia.users.data.UserRepositoryImpl
import id.sosialpedia.users.domain.UserRepository
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 26/05/22
 */
val usersModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
}