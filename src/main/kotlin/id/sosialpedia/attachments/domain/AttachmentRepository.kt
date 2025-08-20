package id.sosialpedia.attachments.domain

import id.sosialpedia.attachments.domain.model.Attachment
import id.sosialpedia.attachments.routes.model.CreateAttachmentRequest

/**
 * Interface untuk mengelola operasi lampiran (attachment).
 */
interface AttachmentRepository {
    /**
     * Menambahkan satu atau lebih lampiran untuk sebuah post.
     * @param postId ID dari post yang memiliki lampiran.
     * @param attachments Daftar objek CreateAttachmentRequest.
     * @return Result yang berisi daftar objek Attachment yang berhasil dibuat.
     */
    suspend fun addAttachments(postId: String, attachments: List<CreateAttachmentRequest>): Result<List<Attachment>>

    /**
     * Mengambil semua lampiran untuk sebuah post.
     * @param postId ID dari post.
     * @return Result yang berisi daftar Attachment.
     */
    suspend fun getAttachmentsForPost(postId: String): Result<List<Attachment>>

    /**
     * Menghapus sebuah lampiran.
     * @param attachmentId ID dari lampiran yang akan dihapus.
     * @return Result yang menandakan apakah operasi berhasil.
     */
    suspend fun removeAttachment(attachmentId: String): Result<Boolean>
}