package id.sosialpedia.reaction.di

import id.sosialpedia.reaction.data.ReactionRepositoryImpl
import id.sosialpedia.reaction.domain.ReactionRepository
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
val reactionModule = module {
    single<ReactionRepository> { ReactionRepositoryImpl(get()) }
}