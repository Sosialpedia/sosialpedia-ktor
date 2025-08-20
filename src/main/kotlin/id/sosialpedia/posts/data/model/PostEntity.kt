package id.sosialpedia.posts.data.model

import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object PostEntity : UUIDTable("posts") {
    val userId = uuid("user_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val content = text("content")
    val haveAttachment = bool("have_attachment").default(false)
    val createdAt = long("created_at")
}