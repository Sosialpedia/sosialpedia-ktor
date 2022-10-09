package id.sosialpedia.posts.data.model

import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Table

object PostEntity : Table("post") {
    val id = varchar("id", 50).uniqueIndex()
    val userId = varchar("user_id", 50).references(UserEntity.id)
    val content = text("content")
    val haveAttach = bool("have_attachment")
    val createdAt = long("created_at")
}