package id.sosialpedia.attachments.data

import id.sosialpedia.attachments.data.model.AttachmentEntity
import id.sosialpedia.attachments.domain.AttachmentRepository
import id.sosialpedia.attachments.domain.model.Attachment
import id.sosialpedia.attachments.routes.model.CreateAttachmentRequest
import id.sosialpedia.util.toShuffledMD5
import io.ktor.network.tls.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */
class AttachmentRepositoryImpl(private val db: Database) : AttachmentRepository {
    override suspend fun addAttachment(createAttachmentRequest: CreateAttachmentRequest): Result<Attachment> {
        return newSuspendedTransaction(db = db) {
            try {
                val insert = AttachmentEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(25)
                    it[linkUrl] = createAttachmentRequest.linkUrl
                    it[type] = createAttachmentRequest.type
                    it[postId] = createAttachmentRequest.postId
                    it[commentId] = createAttachmentRequest.commentId
                    it[childCommentId] = createAttachmentRequest.childCommentId
                }
                val result = insert.resultedValues!!.map {
                    Attachment(
                        id = it[AttachmentEntity.id],
                        linkUrl = it[AttachmentEntity.linkUrl],
                        type = it[AttachmentEntity.type],
                        postId = it[AttachmentEntity.postId],
                        commentId = it[AttachmentEntity.commentId],
                        childCommentId = it[AttachmentEntity.childCommentId],
                    )
                }.first()
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun removeAttachment(attachmentId: String, referenceId: String): Result<String> {
        return newSuspendedTransaction {
            try {
                AttachmentEntity.deleteWhere {
                    AttachmentEntity.id eq attachmentId
                }
                Result.success("Attachment successfully deleted")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}