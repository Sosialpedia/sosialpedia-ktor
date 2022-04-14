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
}