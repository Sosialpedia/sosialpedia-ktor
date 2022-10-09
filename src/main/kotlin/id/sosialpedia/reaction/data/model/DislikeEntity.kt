package id.sosialpedia.reaction.data.model

import id.sosialpedia.comments.data.model.CommentEntity
import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Table

object DislikeEntity : Table("dislike") {
    val id = varchar("id", 20).uniqueIndex()
    val userId = varchar("user_id", 16).references(UserEntity.id)
    val postId = varchar("post_id", 20).references(PostEntity.id)
    val commentId = varchar("comment_id", 20).references(CommentEntity.id)
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}