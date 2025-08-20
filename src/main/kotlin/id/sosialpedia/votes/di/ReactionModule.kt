package id.sosialpedia.votes.di

import id.sosialpedia.votes.data.VoteRepositoryImpl
import id.sosialpedia.votes.domain.VoteRepository
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
val voteModule = module {
    single<VoteRepository> { VoteRepositoryImpl(get()) }
}