package id.sosialpedia.reaction.data.model

import id.sosialpedia.comments.data.model.CommentsEntity
import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.users.data.model.UsersEntity
import org.jetbrains.exposed.sql.Table

object DislikesEntity : Table("dislikes") {
    val id = varchar("id", 20).uniqueIndex()
    val userId = varchar("user_id", 16).references(UsersEntity.id)
    val postId = varchar("post_id", 20).references(PostsEntity.id)
    val commentId = varchar("comment_id", 20).references(CommentsEntity.id)
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}