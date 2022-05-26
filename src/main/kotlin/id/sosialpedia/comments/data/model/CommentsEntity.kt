package id.sosialpedia.comments.data.model

import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.users.data.model.UsersEntity
import org.jetbrains.exposed.sql.Table

object CommentsEntity : Table("comments") {
    val id = varchar("id", 20).uniqueIndex()
    val userId = varchar("user_id", 16).references(UsersEntity.id)
    val content = text("content")
    val postId = varchar("post_id", 20).references(PostsEntity.id)
    val haveAttach = bool("have_attachment")
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}