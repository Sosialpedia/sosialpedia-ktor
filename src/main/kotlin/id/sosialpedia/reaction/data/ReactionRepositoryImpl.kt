package id.sosialpedia.reaction.data

import id.sosialpedia.comments.data.model.ChildCommentEntity
import id.sosialpedia.comments.data.model.CommentEntity
import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.reaction.data.model.DislikeEntity
import id.sosialpedia.reaction.data.model.LikeEntity
import id.sosialpedia.reaction.domain.ReactionRepository
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
                LikeEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[LikeEntity.postId] = postId
                    it[LikeEntity.userId] = userId
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
                LikeEntity.deleteWhere { (LikeEntity.postId eq postId) and (LikeEntity.userId eq userId) }
                Result.success("Successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addLikeToComment(commentId: String, userId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                LikeEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[LikeEntity.commentId] = commentId
                    it[LikeEntity.userId] = userId
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
                LikeEntity.deleteWhere { (LikeEntity.commentId eq commentId) and (LikeEntity.userId eq userId) }
                Result.success("Successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun countTotalCommentFromPost(postId: String): Long {
        return newSuspendedTransaction {
            try {
                val commentCount = (PostEntity innerJoin CommentEntity)
                    .slice(CommentEntity.id.count())
                    .select(CommentEntity.postId eq postId)
                    .groupBy(CommentEntity.id)
                    .count()
                val childCommentCount = (CommentEntity innerJoin ChildCommentEntity)
                    .slice(ChildCommentEntity.id)
                    .select((ChildCommentEntity.commentId eq CommentEntity.id) and (ChildCommentEntity.postId eq postId))
                    .groupBy(ChildCommentEntity.id)
                    .count()

                commentCount + childCommentCount
            } catch (e: Exception) {
                e.printStackTrace()
                0L
            }
        }
    }

    override suspend fun countTotalLikeFromPost(postId: String): Long {
        return newSuspendedTransaction {
            try {
                (PostEntity innerJoin LikeEntity)
                    .slice(LikeEntity.id.count())
                    .select(LikeEntity.postId eq postId)
                    .groupBy(LikeEntity.id)
                    .count()
            } catch (e: Exception) {
                e.printStackTrace()
                0L
            }
        }
    }

    override suspend fun countTotalDislikeFromPost(postId: String): Long {
        return newSuspendedTransaction {
            try {
                (PostEntity innerJoin DislikeEntity)
                    .slice(DislikeEntity.id.count())
                    .select(DislikeEntity.postId eq postId)
                    .groupBy(DislikeEntity.id)
                    .count()
            } catch (e: Exception) {
                e.printStackTrace()
                0L
            }
        }
    }

    override suspend fun countTotalLikeFromComment(commentId: String): Long {
        return newSuspendedTransaction {
            try {
                (CommentEntity innerJoin LikeEntity)
                    .slice(LikeEntity.id.count())
                    .select(LikeEntity.commentId eq commentId)
                    .groupBy(LikeEntity.id)
                    .count()
            } catch (e: Exception) {
                e.printStackTrace()
                0L
            }
        }
    }

    override suspend fun countTotalDislikeFromComment(commentId: String): Long {
        return newSuspendedTransaction {
            try {
                (CommentEntity innerJoin DislikeEntity)
                    .slice(DislikeEntity.id.count())
                    .select(DislikeEntity.commentId eq commentId)
                    .groupBy(DislikeEntity.id)
                    .count()
            } catch (e: Exception) {
                e.printStackTrace()
                0L
            }
        }
    }
}