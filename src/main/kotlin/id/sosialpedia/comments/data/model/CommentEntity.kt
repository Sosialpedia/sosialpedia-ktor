package id.sosialpedia.comments.data.model

import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Table

object CommentEntity : Table("comment") {
    val id = varchar("id", 50).uniqueIndex()
    val userId = varchar("user_id", 50).references(UserEntity.id)
    val postId = varchar("post_id", 50).references(PostEntity.id)
    val content = text("content")
    val haveAttach = bool("have_attachment")
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}