package id.sosialpedia.attachments.data

import id.sosialpedia.attachments.data.model.AttachmentEntity
import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.attachments.domain.model.Attachment
import id.sosialpedia.attachments.routes.model.CreateAttachmentRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * Implementasi dari AttachmentRepository.
 */
class AttachmentRepositoryImpl(private val db: Database) : AttachmentRepository {

    // Helper function untuk mengubah baris database menjadi objek domain Attachment.
    private fun toAttachment(row: ResultRow): Attachment {
        return Attachment(
            id = row[AttachmentEntity.id].value.toString(),
            postId = row[AttachmentEntity.postId].toString(),
            attachmentUrl = row[AttachmentEntity.attachmentUrl],
            attachmentType = row[AttachmentEntity.attachmentType],
            createdAt = row[AttachmentEntity.createdAt]
        )
    }

    override suspend fun addAttachments(
        postId: String,
        attachments: List<CreateAttachmentRequest>
    ): Result<List<Attachment>> {
        return newSuspendedTransaction(db = db) {
            try {
                val newAttachments = mutableListOf<Attachment>()

                // Lakukan batch insert untuk efisiensi jika ada banyak lampiran.
                attachments.forEach { req ->
                    val insertStatement = AttachmentEntity.insert {
                        it[AttachmentEntity.postId] = UUID.fromString(postId)
                        it[attachmentUrl] = req.attachmentUrl
                        it[attachmentType] = req.attachmentType
                        it[createdAt] = System.currentTimeMillis()
                    }
                    newAttachments.add(toAttachment(insertStatement.resultedValues!!.first()))
                }
                Result.success(newAttachments)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getAttachmentsForPost(postId: String): Result<List<Attachment>> {
        return newSuspendedTransaction(db = db) {
            try {
                val result = AttachmentEntity
                    .selectAll()
                    .where { AttachmentEntity.postId eq UUID.fromString(postId) }
                    .map(::toAttachment)
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun removeAttachment(attachmentId: String): Result<Boolean> {
        return newSuspendedTransaction(db = db) {
            try {
                // Cukup hapus berdasarkan ID uniknya.
                // Otorisasi (apakah user boleh menghapus) sebaiknya dilakukan di lapisan service/use case.
                val deletedRows = AttachmentEntity.deleteWhere {
                    AttachmentEntity.id eq UUID.fromString(attachmentId)
                }
                Result.success(deletedRows > 0)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
