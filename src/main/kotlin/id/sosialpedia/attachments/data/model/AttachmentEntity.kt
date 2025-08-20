package id.sosialpedia.attachments.data.model

import id.sosialpedia.posts.data.model.PostEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

/**
 * @author  Samuel Mareno
 * @created 08/08/2025
 */
object AttachmentEntity : UUIDTable("attachments") {
    val postId = uuid("post_id").references(PostEntity.id, onDelete = ReferenceOption.CASCADE)

    val attachmentUrl = varchar("attachment_url", 255)

    val attachmentType = varchar("attachment_type", 20)

    val createdAt = long("created_at")
}