package id.sosialpedia.votes.data

import id.sosialpedia.votes.data.model.VoteEntity
import id.sosialpedia.votes.domain.VoteRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * Implementasi dari VoteRepository.
 */
class VoteRepositoryImpl(private val db: Database) : VoteRepository {

    override suspend fun addOrUpdateVote(
        userId: String,
        postId: String?,
        commentId: String?,
        voteType: Short
    ): Result<Boolean> {
        // Validasi input: vote harus untuk post ATAU comment, tidak bisa keduanya atau tidak sama sekali.
        if ((postId == null && commentId == null) || (postId != null && commentId != null)) {
            return Result.failure(IllegalArgumentException("Vote must target either a post or a comment, not both or none."))
        }

        return newSuspendedTransaction(db = db) {
            try {
                val userUUID = UUID.fromString(userId)
                val postUUID = postId?.let { UUID.fromString(it) }
                val commentUUID = commentId?.let { UUID.fromString(it) }

                // Buat klausa 'where' berdasarkan target vote (post atau comment)
                val whereClause: Op<Boolean> = if (postUUID != null) {
                    (VoteEntity.userId eq userUUID) and (VoteEntity.postId eq postUUID)
                } else {
                    (VoteEntity.userId eq userUUID) and (VoteEntity.commentId eq commentUUID)
                }

                // Cek apakah user sudah pernah vote pada item ini sebelumnya.
                val existingVote = VoteEntity.select(whereClause).firstOrNull()

                if (existingVote == null) {
                    // KASUS 1: User belum pernah vote. Buat vote baru.
                    VoteEntity.insert {
                        it[VoteEntity.userId] = userUUID
                        it[VoteEntity.postId] = postUUID
                        it[VoteEntity.commentId] = commentUUID
                        it[VoteEntity.voteType] = voteType
                        it[createdAt] = System.currentTimeMillis()
                    }
                } else {
                    // KASUS 2: User sudah pernah vote.
                    val existingVoteType = existingVote[VoteEntity.voteType]
                    if (existingVoteType == voteType) {
                        // Jika user mengklik tombol yang sama (misal: like lagi), hapus vote-nya.
                        VoteEntity.deleteWhere { whereClause }
                    } else {
                        // Jika user mengklik tombol yang berbeda (misal: dislike padahal sudah like), ubah vote-nya.
                        VoteEntity.update({ whereClause }) {
                            it[VoteEntity.voteType] = voteType
                        }
                    }
                }
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}