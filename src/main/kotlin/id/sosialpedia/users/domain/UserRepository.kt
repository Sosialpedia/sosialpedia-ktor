package id.sosialpedia.users.domain

import id.sosialpedia.users.domain.model.RegisteredUser
import id.sosialpedia.users.domain.model.User
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.users.routes.model.UserInfoRequest

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
interface UserRepository {

    suspend fun getAllUsers(): Result<List<User>>

    suspend fun getUserById(userId: String): Result<User>

    suspend fun getUserByUsername(username: String): Result<User>

    suspend fun getUserByEmail(email: String): Result<User>

    suspend fun registerUser(createUserRequest: CreateUserRequest): Result<RegisteredUser>

    suspend fun updateUserLogin(id: String, ipAddress: String, device: String): Result<Unit>

    suspend fun updateUserInfo(userId: String, userInfoRequest: UserInfoRequest): Result<String>

    suspend fun deleteUser(id: String): Result<Unit>
}