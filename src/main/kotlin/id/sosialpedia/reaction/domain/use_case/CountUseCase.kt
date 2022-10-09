package id.sosialpedia.reaction.domain.use_case

/**
 * @author Samuel Mareno
 * @Date 09/10/22
 */
data class CountUseCase(
    val commentFromPost: CommentFromPost,
    val likesFromPost: LikesFromPost,
    val dislikesFromPost: DislikesFromPost,
    val likesFromComment: LikesFromComment,
    val dislikesFromComment: DislikesFromComment,
)
