package id.sosialpedia.posts.data.model

import id.sosialpedia.users.data.model.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object PostsEntity : Table("posts") {
    val id = varchar("id", 20).uniqueIndex()
    val userId = varchar("user_id", 16).references(Users.id)
    val content = text("content")
    val haveAttach = bool("have_attachment")
    val createdAt = datetime("created_at")
}