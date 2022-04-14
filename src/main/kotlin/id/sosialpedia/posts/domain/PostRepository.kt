package id.sosialpedia.posts.domain

import id.sosialpedia.posts.domain.model.Post
import id.sosialpedia.posts.routes.model.CreatePostRequest

interface PostRepository {

    suspend fun getAllPostByUserId(userId: String): List<Post>

    suspend fun createPost(postRequest: CreatePostRequest): Result<Post>

    suspend fun deletePostById(postId: String, userId: String): Result<Boolean>
}