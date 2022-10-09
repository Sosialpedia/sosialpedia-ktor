package id.sosialpedia.posts.data

import id.sosialpedia.core.domain.OwnerRepository
import id.sosialpedia.posts.data.model.PostEntity
import id.sosialpedia.posts.domain.PostRepository
import id.sosialpedia.posts.domain.model.Post
import id.sosialpedia.posts.routes.model.CreatePostRequest
import id.sosialpedia.reaction.domain.use_case.CountUseCase
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class PostRepositoryImpl(
    private val db: Database,
    private val ownerRepository: OwnerRepository,
    private val countUseCase: CountUseCase
) : PostRepository {

    override suspend fun getAllPostByUserId(userId: String): List<Post> {
        return newSuspendedTransaction {
            try {
                val result = (PostEntity innerJoin UserEntity)
                    .select {
                        PostEntity.userId eq userId
                    }.sortedBy {
                        it[PostEntity.createdAt]
                    }
                val owner = ownerRepository.getOwner(userId)

                result.map {
                    Post(
                        id = it[PostEntity.id],
                        userId = it[PostEntity.userId],
                        content = it[PostEntity.content],
                        haveAttachment = it[PostEntity.haveAttach],
                        createdAt = it[PostEntity.createdAt],
                        owner = owner,
                        totalLike = countUseCase.likesFromPost(it[PostEntity.id]),
                        totalDislike = countUseCase.dislikesFromPost(it[PostEntity.id]),
                        totalComment = countUseCase.commentFromPost(it[PostEntity.id])
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    override suspend fun createPost(postRequest: CreatePostRequest): Result<Post> {
        return newSuspendedTransaction(db = db) {
            try {
                val insert = PostEntity.insert {
                    it[userId] = postRequest.userId
                    it[content] = postRequest.content
                    it[haveAttach] = postRequest.haveAttachment
                    it[createdAt] = System.currentTimeMillis()
                }
                val post = insert.resultedValues!!.map {
                    val owner = ownerRepository.getOwner(postRequest.userId)
                    Post(
                        id = "secretId",
                        userId = it[PostEntity.userId],
                        content = it[PostEntity.content],
                        haveAttachment = it[PostEntity.haveAttach],
                        createdAt = it[PostEntity.createdAt],
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

    override suspend fun deletePostById(postId: String, userId: String) {
        return newSuspendedTransaction {
            try {
                PostEntity.deleteWhere { (PostEntity.id eq postId) and (PostEntity.userId eq userId) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}