package id.sosialpedia.attachments.data.model

import id.sosialpedia.comments.data.model.ChildCommentsEntity
import id.sosialpedia.comments.data.model.CommentsEntity
import id.sosialpedia.comments.domain.model.ChildComment
import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.users.data.model.Users
import org.jetbrains.exposed.sql.Table

/**
 * @author Samuel Mareno
 * @Date 19/04/22
 */
object AttachmentEntity : Table("attachments") {
    val id = varchar("id", 25)
    val linkUrl = varchar("link_url", 255)
    val type = varchar("type", 5)
    val postId = varchar("post_id", 20).references(PostsEntity.id).nullable()
    val commentId = varchar("comment_id", 20).references(CommentsEntity.id).nullable()
    val childCommentId = varchar("child_comment_id", 20).references(ChildCommentsEntity.id).nullable()

    override val primaryKey = PrimaryKey(id)
}