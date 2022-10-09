package id.sosialpedia.comments.data.model

import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Table

object ChildCommentEntity : Table("child_comment") {
    val id = varchar("id", 20).uniqueIndex()
    val userId = varchar("user_id", 16).references(UserEntity.id)
    val postId = varchar("post_id", 20).references(PostEntity.id)
    val content = text("content")
    val commentId = varchar("comment_id", 20).references(CommentEntity.id)
    val haveAttach = bool("have_attachment")
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}