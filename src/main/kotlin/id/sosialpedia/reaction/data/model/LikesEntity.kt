package id.sosialpedia.reaction.data.model

import id.sosialpedia.comments.data.model.CommentsEntity
import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.users.data.model.Users
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.*

object LikesEntity : Table("likes") {
    val id = varchar("id", 20)
        .uniqueIndex()
        .default(UUID.randomUUID().toShuffledMD5(20))
    val userId = varchar("user_id", 16).references(Users.id)
    val postId = varchar("post_id", 20).references(PostsEntity.id)
    val commentId = varchar("comment_id", 20).references(CommentsEntity.id)
    val createdAt = datetime("created_at").default(LocalDateTime.now())

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}