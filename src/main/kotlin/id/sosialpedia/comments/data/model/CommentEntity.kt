package id.sosialpedia.comments.data.model

import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object CommentEntity : UUIDTable("comments") {
    // Foreign key ke tabel posts
    val postId = uuid("post_id").references(PostEntity.id, onDelete = ReferenceOption.CASCADE)

    // Foreign key ke tabel users
    val userId = uuid("user_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)

    // KUNCI untuk nested comments: Foreign key yang merujuk ke tabel ini sendiri.
    // Dibuat nullable karena komentar level 1 (parent) tidak punya parent_id.
    val parentCommentId = uuid("parent_comment_id").references(id, onDelete = ReferenceOption.CASCADE).nullable()

    val content = text("content")
    val haveAttachment = bool("have_attachment").default(false)
    val createdAt = long("created_at")
}