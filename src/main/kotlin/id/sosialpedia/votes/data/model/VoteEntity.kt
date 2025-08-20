package id.sosialpedia.votes.data.model

import id.sosialpedia.comments.data.model.CommentEntity
import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

/**
 * Entity untuk tabel 'votes'.
 * Mengelola data like (vote_type = 1) dan dislike (vote_type = -1)
 * untuk post dan komentar.
 */
object VoteEntity : UUIDTable("votes") {
    // Foreign key ke tabel users
    val userId = uuid("user_id").references(UserEntity.id, onDelete = ReferenceOption.CASCADE)

    // Foreign key ke tabel posts (nullable karena vote bisa untuk comment)
    val postId = uuid("post_id").references(PostEntity.id, onDelete = ReferenceOption.CASCADE).nullable()

    // Foreign key ke tabel comments (nullable karena vote bisa untuk post)
    val commentId = uuid("comment_id").references(CommentEntity.id, onDelete = ReferenceOption.CASCADE).nullable()

    // Kolom untuk membedakan like (1) dan dislike (-1)
    val voteType = short("vote_type")

    val createdAt = long("created_at")
}