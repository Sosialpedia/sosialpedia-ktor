package id.sosialpedia.comments.data

import id.sosialpedia.comments.data.model.ChildCommentsEntity
import id.sosialpedia.comments.data.model.CommentsEntity
import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.comments.domain.model.Comment
import id.sosialpedia.comments.routes.model.CommentRequest
import id.sosialpedia.posts.data.model.DislikesEntity
import id.sosialpedia.posts.data.model.LikesEntity
import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.util.execAndMap
import id.sosialpedia.util.toFormattedString
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
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
                    val totalComment = (CommentsEntity innerJoin ChildCommentsEntity)
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
                        commentId = null,
                        haveAttachment = it[CommentsEntity.haveAttach],
                        createdAt = it[CommentsEntity.createdAt].toFormattedString(),
                        totalLike = totalLike,
                        totalDislike = totalDislike,
                        totalComment = totalComment
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
                    it[createdAt] = LocalDateTime.now()
                }
                val result = insert.resultedValues!!.map {
                    Comment(
                        id = it[CommentsEntity.id],
                        userId = it[CommentsEntity.userId],
                        content = it[CommentsEntity.content],
                        postId = it[CommentsEntity.postId],
                        commentId = null,
                        haveAttachment = it[CommentsEntity.haveAttach],
                        createdAt = it[CommentsEntity.createdAt].toFormattedString()
                    )
                }.first()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteCommentToPost(postId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCommentToComment(commentId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun testingTransaction(): Result<List<String>> {
        val result = arrayListOf<String>()
        return newSuspendedTransaction {
            ("""
                SELECT posts.id,
                       posts.content,
                       users.id,
                       users.username,
                       (SELECT COUNT(comments.id) FROM comments WHERE comments.post_id = posts.id)                                 as total_comment,
                       (SELECT COUNT(likes.id)
                        FROM likes
                        WHERE likes.post_id = posts.id
                          AND likes.comment_id IS NULL)                                                                            as total_like,
                       (SELECT COUNT(dislikes.id)
                        FROM dislikes
                        WHERE dislikes.post_id = posts.id
                          AND dislikes.comment_id IS NULL)                                                                         as total_dislike
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