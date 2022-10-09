package id.sosialpedia.reaction.di

import id.sosialpedia.reaction.data.ReactionRepositoryImpl
import id.sosialpedia.reaction.domain.ReactionRepository
import id.sosialpedia.reaction.domain.use_case.*
import org.koin.dsl.module

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
val reactionModule = module {
    single<ReactionRepository> { ReactionRepositoryImpl(get()) }
    single {
        CountUseCase(
            commentFromPost = CommentFromPost(get()),
            likesFromPost = LikesFromPost(get()),
            dislikesFromPost = DislikesFromPost(get()),
            likesFromComment = LikesFromComment(get()),
            dislikesFromComment = DislikesFromComment(get())
        )
    }
}