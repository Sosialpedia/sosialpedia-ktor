package id.sosialpedia.comments.data.model

import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.users.data.model.Users
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object CommentsEntity : Table("comments") {
    val id = varchar("id", 20).uniqueIndex()
    val userId = varchar("user_id", 16).references(Users.id)
    val content = text("content")
    val postId = varchar("post_id", 20).references(PostsEntity.id)
    val haveAttach = bool("have_attachment")
    val createdAt = datetime("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}