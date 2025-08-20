package id.sosialpedia.posts.data

import id.sosialpedia.core.domain.Owner
import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.posts.domain.PostRepository
import id.sosialpedia.posts.domain.model.Post
import id.sosialpedia.posts.routes.model.CreatePostRequest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.UUIDColumnType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.ResultSet
import java.util.*

/**
 * Implementasi PostRepository yang ditulis ulang menggunakan query SQL standar (mentah)
 * untuk kejelasan dan tujuan pembelajaran.
 */
class PostRepositoryImpl(
    private val db: Database,
) : PostRepository {

    // Helper function untuk mengubah baris hasil query mentah menjadi objek Post.
    private fun toPost(rs: ResultSet): Post {
        return Post(
            id = rs.getString("post_id"),
            userId = rs.getString("user_id"),
            content = rs.getString("content"),
            haveAttachment = rs.getBoolean("have_attachment"),
            createdAt = rs.getLong("created_at"),
            owner = Owner(
                userId = rs.getString("user_id"), // userId dari owner sama dengan post
                username = rs.getString("username"),
                profilePicUrl = rs.getString("profile_pic_url")
            ),
            totalLike = rs.getLong("total_like"),
            totalDislike = rs.getLong("total_dislike"),
            totalComment = rs.getLong("total_comment")
        )
    }

    /**
     * Mengambil semua post dari user tertentu menggunakan query SQL mentah.
     */
    override suspend fun getAllPostByUserId(userId: String): Result<List<Post>> {
        return newSuspendedTransaction(db = db) {
            try {
                // Inilah query SQL standar yang akan kita jalankan.
                // Menggunakan alias (AS) membuat hasil query lebih mudah dibaca.
                val sql = """
                    SELECT
                        p.id AS post_id,
                        p.user_id,
                        p.content,
                        p.have_attachment,
                        p.created_at,
                        u.username,
                        u.profile_pic_url,
                        -- Menghitung jumlah like (vote_type = 1)
                        COUNT(CASE WHEN v.vote_type = 1 THEN 1 END) AS total_like,
                        -- Menghitung jumlah dislike (vote_type = -1)
                        COUNT(CASE WHEN v.vote_type = -1 THEN 1 END) AS total_dislike,
                        -- Menghitung jumlah komentar yang unik
                        COUNT(DISTINCT c.id) AS total_comment
                    FROM
                        posts AS p
                    -- INNER JOIN untuk mendapatkan info user (pemilik post)
                    INNER JOIN
                        users AS u ON p.user_id = u.id
                    -- LEFT JOIN agar post tanpa vote tetap muncul
                    LEFT JOIN
                        votes AS v ON p.id = v.post_id
                    -- LEFT JOIN agar post tanpa komentar tetap muncul
                    LEFT JOIN
                        comments AS c ON p.id = c.post_id
                    WHERE
                        p.user_id = ?
                    -- GROUP BY untuk mengelompokkan hasil per post
                    GROUP BY
                        p.id, u.id
                    -- Mengurutkan dari yang paling baru
                    ORDER BY
                        p.created_at DESC;
                """.trimIndent()

                val posts = mutableListOf<Post>()

                // Menjalankan query mentah dengan parameter yang aman
                exec(sql, listOf(UUIDColumnType() to UUID.fromString(userId))) { rs ->
                    while (rs.next()) {
                        posts.add(toPost(rs))
                    }
                }
                Result.success(posts)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun createPost(
        userId: String,
        postRequest: CreatePostRequest
    ): Result<Post> {
        TODO("Not yet implemented")
    }

    /**
     * Membuat post baru. Untuk operasi tulis, DSL Exposed masih sangat efisien.
     */
//    override suspend fun createPost(postRequest: CreatePostRequest): Result<Post> {
//        return newSuspendedTransaction(db = db) {
//            try {
//                // Ambil info user untuk dimasukkan ke objek Owner.
//                val ownerInfo = UserEntity
//                    .select(UserEntity.id, UserEntity.username, UserEntity.profilePicUrl)
//                    .where { UserEntity.id eq UUID.fromString(postRequest.userId) }
//                    .firstOrNull() ?: return@newSuspendedTransaction Result.failure(Exception("Owner user not found"))
//
//                val insertStatement = PostEntity.insert {
//                    it[userId] = UUID.fromString(postRequest.userId)
//                    it[content] = postRequest.content
//                    it[haveAttachment] = postRequest.haveAttachment
//                    it[createdAt] = System.currentTimeMillis()
//                }
//
//                // Buat objek Post secara manual karena semua hitungan untuk post baru adalah 0.
//                val newPost = Post(
//                    id = insertStatement[PostEntity.id].value.toString(),
//                    userId = insertStatement[PostEntity.userId].toString(),
//                    content = insertStatement[PostEntity.content],
//                    haveAttachment = insertStatement[PostEntity.haveAttachment],
//                    createdAt = insertStatement[PostEntity.createdAt],
//                    owner = Owner(
//                        userId = ownerInfo[UserEntity.id].value.toString(),
//                        username = ownerInfo[UserEntity.username],
//                        profilePicUrl = ownerInfo[UserEntity.profilePicUrl]
//                    ),
//                    totalLike = 0,
//                    totalDislike = 0,
//                    totalComment = 0
//                )
//                Result.success(newPost)
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//    }

    /**
     * Menghapus post. Operasi delete sederhana ini juga efisien dengan DSL Exposed.
     */
    override suspend fun deletePostById(postId: String, userId: String): Result<Boolean> {
        return newSuspendedTransaction(db = db) {
            try {
                val deletedRows = PostEntity.deleteWhere {
                    (PostEntity.id eq UUID.fromString(postId)) and (PostEntity.userId eq UUID.fromString(userId))
                }
                Result.success(deletedRows > 0)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
