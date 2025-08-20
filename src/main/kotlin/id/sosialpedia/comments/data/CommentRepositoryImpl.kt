package id.sosialpedia.comments.data

import id.sosialpedia.comments.data.model.CommentEntity
import id.sosialpedia.comments.domain.CommentRepository
import id.sosialpedia.comments.domain.model.ChildComment
import id.sosialpedia.comments.domain.model.Comment
import id.sosialpedia.comments.routes.model.ChildCommentRequest
import id.sosialpedia.comments.routes.model.CommentRequest
import id.sosialpedia.users.data.model.UserEntity
import id.sosialpedia.votes.data.model.VoteEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * Refactored implementation of CommentRepository.
 * This version uses a single 'comments' table with a self-referencing foreign key
 * to handle nested comments efficiently.
 * @author Samuel Mareno
 */
class CommentRepositoryImpl(
    private val db: Database,
) : CommentRepository {

    private fun toComment(
        row: ResultRow,
        likes: Long,
        dislikes: Long,
        replies: List<Comment> = emptyList()
    ): Comment {
        return Comment(
            id = row[CommentEntity.id].value.toString(),
            postId = row[CommentEntity.postId].toString(),
            userId = row[CommentEntity.userId].toString(),
            username = row[UserEntity.username],
            userProfilePicUrl = row[UserEntity.profilePicUrl],
            content = row[CommentEntity.content],
            parentCommentId = row[CommentEntity.parentCommentId]?.toString(),
            haveAttachment = row[CommentEntity.haveAttachment],
            createdAt = row[CommentEntity.createdAt],
            totalLikes = likes,
            totalDislikes = dislikes,
            replies = replies
        )
    }

    /**
     * Mengambil semua komentar untuk sebuah post dan menyusunnya dalam hierarki.
     * Dilakukan dalam 2 langkah utama untuk efisiensi:
     * 1. Ambil SEMUA data mentah (komentar & vote) dari DB dalam beberapa query efisien.
     * 2. Susun struktur pohon (parent-child) di memori aplikasi.
     */
    override suspend fun getCommentsFromPost(postId: String): Result<List<Comment>> {
        return newSuspendedTransaction(db = db) {
            try {
                // Langkah 1.1: Ambil semua komentar untuk post ini, gabungkan dengan info user.
                val allCommentsData = (CommentEntity innerJoin UserEntity)
                    .selectAll()
                    .where { CommentEntity.postId eq UUID.fromString(postId) }
                    .orderBy(CommentEntity.createdAt, SortOrder.ASC)
                    .toList()

                if (allCommentsData.isEmpty()) {
                    return@newSuspendedTransaction Result.success(emptyList())
                }

                val commentIds = allCommentsData.map { it[CommentEntity.id].value }

                // Langkah 1.2: Ambil semua vote untuk komentar-komentar tersebut dalam satu query.
                val votes = VoteEntity
                    .select(VoteEntity.commentId, VoteEntity.voteType)
                    .where { VoteEntity.commentId inList commentIds }
                    .groupBy { it[VoteEntity.commentId] }
                    .mapValues { entry ->
                        val likeCount = entry.value.count { it[VoteEntity.voteType].toInt() == 1 }.toLong()
                        val dislikeCount = entry.value.count { it[VoteEntity.voteType].toInt() == -1 }.toLong()
                        likeCount to dislikeCount
                    }

                // Langkah 2: Susun struktur pohon di memori.
                val commentMap = allCommentsData.associate { row ->
                    val commentId = row[CommentEntity.id].value
                    val (likes, dislikes) = votes.getOrDefault(commentId, 0L to 0L)
                    commentId to toComment(row, likes, dislikes)
                }.toMutableMap()

                val rootComments = mutableListOf<Comment>()

                commentMap.values.forEach { comment ->
                    val parentId = comment.parentCommentId?.let { UUID.fromString(it) }
                    if (parentId != null) {
                        // Ini adalah balasan, cari induknya.
                        val parent = commentMap[parentId]
                        if (parent != null) {
                            // Tambahkan balasan ini ke daftar balasan induknya.
                            val updatedParent = parent.copy(replies = parent.replies + comment)
                            commentMap[parentId] = updatedParent
                        }
                    } else {
                        // Ini adalah komentar level 1 (root).
                        rootComments.add(comment)
                    }
                }

                // Hasil akhirnya adalah rootComments yang sudah berisi balasan yang telah disusun ulang dari map.
                val finalResult = rootComments.mapNotNull { commentMap[UUID.fromString(it.id)] }

                Result.success(finalResult)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun addComment(commentRequest: CommentRequest): Result<Comment> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(
        commentId: String, postId: String, userId: String
    ): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getChildComments(commentId: String): Result<List<ChildComment>> {
        TODO("Not yet implemented")
    }

    override suspend fun addChildComment(childCommentRequest: ChildCommentRequest): Result<ChildComment> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteChildComment(
        childCommentId: String, commentId: String, userId: String
    ): Result<String> {
        TODO("Not yet implemented")
    }


}