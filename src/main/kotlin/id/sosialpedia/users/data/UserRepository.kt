package id.sosialpedia.users.data

import id.sosialpedia.users.routes.model.UserInfoRequest

interface UserRepository {

    suspend fun getAllUsers(): List<String>

    suspend fun getUserById(id: String): String

    suspend fun registerUser(): Result<Unit>

    suspend fun updateUserLogin(id: String, ipAddress: String, device: String): Result<Unit>

    suspend fun updateUserInfo(userInfoRequest: UserInfoRequest): Result<String>

    suspend fun deleteUser(id: String): Result<Unit>
}