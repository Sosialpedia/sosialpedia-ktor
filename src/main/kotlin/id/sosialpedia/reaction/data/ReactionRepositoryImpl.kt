package id.sosialpedia.reaction.data

import id.sosialpedia.reaction.data.model.LikesEntity
import id.sosialpedia.reaction.domain.ReactionRepository
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
class ReactionRepositoryImpl(private val db: Database) : ReactionRepository {

    override suspend fun addLikeToPost(postId: String, userId: String): Result<String> {
        return newSuspendedTransaction(db = db) {
            try {
                LikesEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[LikesEntity.postId] = postId
                    it[LikesEntity.userId] = userId
                    it[createdAt] = System.currentTimeMillis()
                }
                Result.success("Successfully added")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun removeLikeFromPost(postId: String, userId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                LikesEntity.deleteWhere { (LikesEntity.postId eq postId) and (LikesEntity.userId eq userId) }
                Result.success("Successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addLikeToComment(commentId: String, userId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                LikesEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[LikesEntity.commentId] = commentId
                    it[LikesEntity.userId] = userId
                    it[createdAt] = System.currentTimeMillis()
                }
                Result.success("Successfully added")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun removeLikeFromComment(commentId: String, userId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                LikesEntity.deleteWhere { (LikesEntity.commentId eq commentId) and (LikesEntity.userId eq userId) }
                Result.success("Successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}