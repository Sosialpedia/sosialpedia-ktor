package id.sosialpedia.comments.data

import id.sosialpedia.comments.data.model.ChildCommentsEntity
import id.sosialpedia.comments.data.model.CommentsEntity
import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.comments.domain.model.ChildComment
import id.sosialpedia.comments.domain.model.Comment
import id.sosialpedia.comments.routes.model.ChildCommentRequest
import id.sosialpedia.comments.routes.model.CommentRequest
import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.reaction.data.model.DislikesEntity
import id.sosialpedia.reaction.data.model.LikesEntity
import id.sosialpedia.util.execAndMap
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class CommentRepositoryImpl(private val db: Database) : CommentRepository {

    override suspend fun getCommentsFromPost(postId: String): Result<List<Comment>> {
        return newSuspendedTransaction {
            try {
                val query = (CommentsEntity innerJoin PostsEntity)
                    //.slice(CommentsEntity.id, CommentsEntity.content)
                    .select(PostsEntity.id eq postId)
                    .orderBy(CommentsEntity.createdAt)
                val result = query.map {
                    val totalLike = (CommentsEntity innerJoin LikesEntity)
                        .slice(LikesEntity.id.count())
                        .select(LikesEntity.commentId eq it[CommentsEntity.id])
                        .groupBy(LikesEntity.id)
                        .count()
                        .toInt()
                    val totalDislike = (CommentsEntity innerJoin DislikesEntity)
                        .slice(DislikesEntity.id.count())
                        .select(DislikesEntity.commentId eq it[CommentsEntity.id])
                        .groupBy(DislikesEntity.id)
                        .count()
                        .toInt()
                    val totalChildComment = (CommentsEntity innerJoin ChildCommentsEntity)
                        .slice(ChildCommentsEntity.id.count())
                        .select(ChildCommentsEntity.commentId eq it[CommentsEntity.id])
                        .groupBy(ChildCommentsEntity.id)
                        .count()
                        .toInt()
                    Comment(
                        id = it[CommentsEntity.id],
                        userId = it[CommentsEntity.userId],
                        content = it[CommentsEntity.content],
                        postId = it[PostsEntity.id],
                        haveAttachment = it[CommentsEntity.haveAttach],
                        createdAt = it[CommentsEntity.createdAt],
                        totalLike = totalLike,
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
                val insert = CommentsEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(20)
                    it[userId] = commentRequest.userId
                    it[content] = commentRequest.content
                    it[postId] = commentRequest.postId
                    it[haveAttach] = commentRequest.haveAttachment
                    it[createdAt] = System.currentTimeMillis()
                }
                val result = insert.resultedValues!!.map {
                    Comment(
                        id = it[CommentsEntity.id],
                        userId = it[CommentsEntity.userId],
                        content = it[CommentsEntity.content],
                        postId = it[CommentsEntity.postId],
                        haveAttachment = it[CommentsEntity.haveAttach],
                        createdAt = it[CommentsEntity.createdAt]
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
                CommentsEntity.deleteWhere {
                    (CommentsEntity.id eq commentId) and
                            (CommentsEntity.postId eq postId) and
                            (CommentsEntity.userId eq userId)
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
                val result = ChildCommentsEntity
                    .select {
                        ChildCommentsEntity.commentId eq commentId
                    }.orderBy(ChildCommentsEntity.createdAt)
                    .map {
                        ChildComment(
                            id = it[ChildCommentsEntity.id],
                            userId = it[ChildCommentsEntity.userId],
                            postId = it[ChildCommentsEntity.postId],
                            commentId = it[ChildCommentsEntity.commentId],
                            content = it[ChildCommentsEntity.content],
                            haveAttachment = it[ChildCommentsEntity.haveAttach],
                            createdAt = it[ChildCommentsEntity.createdAt]
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
                val insert = ChildCommentsEntity.insert {
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
                        id = it[ChildCommentsEntity.id],
                        userId = it[ChildCommentsEntity.userId],
                        postId = it[ChildCommentsEntity.postId],
                        content = it[ChildCommentsEntity.content],
                        commentId = it[ChildCommentsEntity.commentId],
                        haveAttachment = it[ChildCommentsEntity.haveAttach],
                        createdAt = it[ChildCommentsEntity.createdAt]
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
                ChildCommentsEntity.deleteWhere {
                    (ChildCommentsEntity.id eq childCommentId) and
                            (ChildCommentsEntity.userId eq userId) and
                            (ChildCommentsEntity.commentId eq commentId)
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