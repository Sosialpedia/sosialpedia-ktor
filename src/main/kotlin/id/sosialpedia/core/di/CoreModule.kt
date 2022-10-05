package id.sosialpedia.core.di

import id.sosialpedia.core.data.OwnerRepositoryImpl
import id.sosialpedia.core.domain.OwnerRepository
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 06/10/22
 */
val coreModule = module {
    single<OwnerRepository> { OwnerRepositoryImpl(get()) }
}