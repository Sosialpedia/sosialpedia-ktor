package id.sosialpedia.posts.data

import id.sosialpedia.comments.data.model.CommentsEntity
import id.sosialpedia.core.domain.OwnerRepository
import id.sosialpedia.posts.data.model.PostsEntity
import id.sosialpedia.posts.domain.PostRepository
import id.sosialpedia.posts.domain.model.Post
import id.sosialpedia.posts.routes.model.CreatePostRequest
import id.sosialpedia.users.data.model.UsersEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PostRepositoryImpl(
    private val db: Database,
    private val ownerRepository: OwnerRepository
) : PostRepository {

    override suspend fun getAllPostByUserId(userId: String): List<Post> {
        return newSuspendedTransaction {
            val result = (PostsEntity innerJoin UsersEntity)
                .select {
                    PostsEntity.userId eq userId
                }.sortedBy {
                    it[PostsEntity.createdAt]
                }

            result.map {

//                val totalLike = (PostsEntity innerJoin LikesEntity)
//                    .slice(LikesEntity.id.count())
//                    .select(LikesEntity.postId eq it[PostsEntity.id])
//                    .groupBy(LikesEntity.id)
//                    .count()
//                    .toInt()
//                val totalDislike = (PostsEntity innerJoin DislikesEntity)
//                    .slice(DislikesEntity.id.count())
//                    .select(DislikesEntity.postId eq it[PostsEntity.id])
//                    .groupBy(DislikesEntity.id)
//                    .count()
//                    .toInt()
                val totalComment = (PostsEntity innerJoin CommentsEntity)
                    .slice(CommentsEntity.id.count())
                    .select(CommentsEntity.postId eq it[PostsEntity.id])
                    .groupBy(CommentsEntity.id)
                    .count()
//                val totalChildComment = (CommentsEntity innerJoin ChildCommentsEntity)
//                    .slice(ChildCommentsEntity.id)
//                    .select((ChildCommentsEntity.commentId eq CommentsEntity.id) and (ChildCommentsEntity.postId eq it[PostsEntity.id]))
//                    .groupBy(ChildCommentsEntity.id)
//                    .count()
//                    .toInt()

                val owner = ownerRepository.getOwner(userId)

                Post(
                    id = it[PostsEntity.id],
                    userId = it[PostsEntity.userId],
                    content = it[PostsEntity.content],
                    haveAttachment = it[PostsEntity.haveAttach],
                    createdAt = it[PostsEntity.createdAt],
                    owner = owner,
                    totalLike = 0,
                    totalDislike = 0,
                    totalComment = totalComment
                )
            }
        }
    }

    override suspend fun createPost(postRequest: CreatePostRequest): Result<Post> {
        return newSuspendedTransaction(db = db) {
            try {
                val insert = PostsEntity.insert {
                    it[userId] = postRequest.userId
                    it[content] = postRequest.content
                    it[haveAttach] = postRequest.haveAttachment
                    it[createdAt] = System.currentTimeMillis()
                }
                val post = insert.resultedValues!!.map {
                    val owner = ownerRepository.getOwner(postRequest.userId)
                    Post(
                        id = "secretId",
                        userId = it[PostsEntity.userId],
                        content = it[PostsEntity.content],
                        haveAttachment = it[PostsEntity.haveAttach],
                        createdAt = it[PostsEntity.createdAt],
                        owner = owner,
                        totalComment = 0,
                        totalLike = 0,
                        totalDislike = 0
                    )
                }.first()
                Result.success(post)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }

        }
    }

    override suspend fun deletePostById(postId: String, userId: String): Result<Boolean> {
        return newSuspendedTransaction {
            try {
                PostsEntity.deleteWhere { (PostsEntity.id eq postId) and (PostsEntity.userId eq userId) }
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }

        }
    }
}