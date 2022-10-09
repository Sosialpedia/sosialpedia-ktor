package id.sosialpedia.reaction.domain.use_case

import id.sosialpedia.reaction.domain.ReactionRepository

/**
 * @author Samuel Mareno
 * @Date 09/10/22
 */
class LikesFromPost(
    private val reactionRepository: ReactionRepository
) {
    suspend operator fun invoke(postId: String): Long {
        return reactionRepository.countTotalLikeFromPost(postId)
    }
}