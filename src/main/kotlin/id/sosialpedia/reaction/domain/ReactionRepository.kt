package id.sosialpedia.reaction.domain

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
interface ReactionRepository {

    suspend fun addLikeToPost(postId: String, userId: String): Result<String>

    suspend fun removeLikeFromPost(postId: String, userId: String): Result<String>

    suspend fun addLikeToComment(commentId: String, userId: String): Result<String>

    suspend fun removeLikeFromComment(commentId: String, userId: String): Result<String>

    suspend fun countTotalCommentFromPost(postId: String): Long
    suspend fun countTotalLikeFromPost(postId: String): Long
    suspend fun countTotalDislikeFromPost(postId: String): Long
    suspend fun countTotalLikeFromComment(commentId: String): Long
    suspend fun countTotalDislikeFromComment(commentId: String): Long
}