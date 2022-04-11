package id.sosialpedia.posts.domain

import id.sosialpedia.posts.routes.model.PostRequest
import id.sosialpedia.posts.domain.model.Post

interface PostRepository {

    suspend fun getAllPostByUserId(userId: String): List<Post>

    suspend fun createPost(postRequest: PostRequest): Result<Post>

    suspend fun deletePostById(postId: String, userId: String): Result<Boolean>
}