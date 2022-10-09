package id.sosialpedia.reaction.domain.use_case

import id.sosialpedia.reaction.domain.ReactionRepository

/**
 * @author Samuel Mareno
 * @Date 09/10/22
 */
class DislikesFromComment(
    private val reactionRepository: ReactionRepository
) {
    suspend operator fun invoke(commentId: String): Long {
        return reactionRepository.countTotalDislikeFromComment(commentId)
    }
}