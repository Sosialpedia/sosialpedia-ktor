package id.sosialpedia.attachments.domain

import id.sosialpedia.attachments.domain.model.Attachment
import id.sosialpedia.attachments.routes.model.CreateAttachmentRequest

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */
interface AttachmentRepository {

    suspend fun addAttachment(createAttachmentRequest: CreateAttachmentRequest): Result<Attachment>

    suspend fun removeAttachment(attachmentId: String, referenceId: String): Result<String>
}