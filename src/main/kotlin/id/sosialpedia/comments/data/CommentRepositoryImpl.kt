package id.sosialpedia.comments.data

import id.sosialpedia.comments.data.model.ChildCommentEntity
import id.sosialpedia.comments.data.model.CommentEntity
import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.comments.domain.model.ChildComment
import id.sosialpedia.comments.domain.model.Comment
import id.sosialpedia.comments.routes.model.ChildCommentRequest
import id.sosialpedia.comments.routes.model.CommentRequest
import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.reaction.data.model.DislikeEntity
import id.sosialpedia.reaction.domain.use_case.CountUseCase
import id.sosialpedia.util.execAndMap
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class CommentRepositoryImpl(
    private val db: Database,
    private val countUseCase: CountUseCase
) : CommentRepository {

    override suspend fun getCommentsFromPost(postId: String): Result<List<Comment>> {
        return newSuspendedTransaction {
            try {
                val query = (CommentEntity innerJoin PostEntity)
                    .select(PostEntity.id eq postId)
                    .orderBy(CommentEntity.createdAt)

                val result = query.map {
                    val totalDislike = (CommentEntity innerJoin DislikeEntity)
                        .slice(DislikeEntity.id.count())
                        .select(DislikeEntity.commentId eq it[CommentEntity.id])
                        .groupBy(DislikeEntity.id)
                        .count()
                    val totalChildComment = (CommentEntity innerJoin ChildCommentEntity)
                        .slice(ChildCommentEntity.id.count())
                        .select(ChildCommentEntity.commentId eq it[CommentEntity.id])
                        .groupBy(ChildCommentEntity.id)
                        .count()

                    Comment(
                        id = it[CommentEntity.id],
                        userId = it[CommentEntity.userId],
                        content = it[CommentEntity.content],
                        postId = it[PostEntity.id],
                        haveAttachment = it[CommentEntity.haveAttach],
                        createdAt = it[CommentEntity.createdAt],
                        totalLike = countUseCase.likesFromComment(it[CommentEntity.id]),
                        totalDislike = totalDislike,
                        totalChildComment = totalChildComment
                    )
                }
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addComment(commentRequest: CommentRequest): Result<Comment> {
        return newSuspendedTransaction(db = db) {
            try {
                val insert = CommentEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[userId] = commentRequest.userId
                    it[content] = commentRequest.content
                    it[postId] = commentRequest.postId
                    it[haveAttach] = commentRequest.haveAttachment
                    it[createdAt] = System.currentTimeMillis()
                }
                val result = insert.resultedValues!!.map {
                    Comment(
                        id = it[CommentEntity.id],
                        userId = it[CommentEntity.userId],
                        content = it[CommentEntity.content],
                        postId = it[CommentEntity.postId],
                        haveAttachment = it[CommentEntity.haveAttach],
                        createdAt = it[CommentEntity.createdAt]
                    )
                }.first()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteComment(commentId: String, postId: String, userId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                CommentEntity.deleteWhere {
                    (CommentEntity.id eq commentId) and
                            (CommentEntity.postId eq postId) and
                            (CommentEntity.userId eq userId)
                }
                Result.success("Successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getChildComments(commentId: String): Result<List<ChildComment>> {
        return newSuspendedTransaction {
            try {
                val result = ChildCommentEntity
                    .select {
                        ChildCommentEntity.commentId eq commentId
                    }.orderBy(ChildCommentEntity.createdAt)
                    .map {
                        ChildComment(
                            id = it[ChildCommentEntity.id],
                            userId = it[ChildCommentEntity.userId],
                            postId = it[ChildCommentEntity.postId],
                            commentId = it[ChildCommentEntity.commentId],
                            content = it[ChildCommentEntity.content],
                            haveAttachment = it[ChildCommentEntity.haveAttach],
                            createdAt = it[ChildCommentEntity.createdAt]
                        )
                    }
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addChildComment(childCommentRequest: ChildCommentRequest): Result<ChildComment> {
        return newSuspendedTransaction {
            try {
                val insert = ChildCommentEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[userId] = childCommentRequest.userId
                    it[postId] = childCommentRequest.postId
                    it[content] = childCommentRequest.content
                    it[commentId] = childCommentRequest.commentId
                    it[haveAttach] = childCommentRequest.haveAttachment
                    it[createdAt] = System.currentTimeMillis()
                }
                val result = insert.resultedValues!!.map {
                    ChildComment(
                        id = it[ChildCommentEntity.id],
                        userId = it[ChildCommentEntity.userId],
                        postId = it[ChildCommentEntity.postId],
                        content = it[ChildCommentEntity.content],
                        commentId = it[ChildCommentEntity.commentId],
                        haveAttachment = it[ChildCommentEntity.haveAttach],
                        createdAt = it[ChildCommentEntity.createdAt]
                    )
                }.first()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteChildComment(childCommentId: String, commentId: String, userId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                ChildCommentEntity.deleteWhere {
                    (ChildCommentEntity.id eq childCommentId) and
                            (ChildCommentEntity.userId eq userId) and
                            (ChildCommentEntity.commentId eq commentId)
                }
                Result.success("Successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun testingTransaction(): Result<List<String>> {
        val result = arrayListOf<String>()
        return newSuspendedTransaction {
            ("""
                SELECT posts.id,
                       posts.content,
                       users.id,
                       users.username,
                       (SELECT COUNT(comments.id) FROM comments 
                       WHERE comments.post_id = posts.id) as total_comment,
                       (SELECT COUNT(likes.id)
                        FROM likes
                        WHERE likes.post_id = posts.id
                          AND likes.comment_id IS NULL) as total_like,
                       (SELECT COUNT(dislikes.id)
                        FROM dislikes
                        WHERE dislikes.post_id = posts.id
                          AND dislikes.comment_id IS NULL) as total_dislike
                FROM posts
                         INNER JOIN users ON (posts.user_id = users.id)
                WHERE (username = 'reno')
            """.trimIndent()).execAndMap { resultSet ->
                result += "username = ${resultSet.getString("users.username")}, postContent = ${resultSet.getString("posts.content")}"
            }
            Result.success(result)
        }
    }
}