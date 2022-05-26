package id.sosialpedia.users.data

import id.sosialpedia.users.data.model.UsersEntity
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.domain.model.User
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.users.routes.model.UserInfoRequest
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
class UserRepositoryImpl(private val db: Database) : UserRepository {
    override suspend fun getAllUsers(): Result<List<User>> {
        return newSuspendedTransaction {
            try {
                val result = UsersEntity.selectAll().map {
                    User(
                        id = it[UsersEntity.id],
                        username = it[UsersEntity.username],
                        email = it[UsersEntity.email],
                        phoneNumber = it[UsersEntity.phoneNumber],
                        profilePic = it[UsersEntity.profilePic],
                        bio = it[UsersEntity.bio],
                        dateBirth = it[UsersEntity.dateBirth],
                        gender = it[UsersEntity.gender],
                        createdAt = it[UsersEntity.createdAt],
                        updatedAt = it[UsersEntity.updatedAt],
                        lastLogin = it[UsersEntity.lastLogin],
                        ipAddress = it[UsersEntity.ipAddress] ?: "can't retrieve Ip Address",
                        device = it[UsersEntity.device]
                    )
                }
                Result.success(result)
            } catch (e: Exception) {
                println("error getUser $e")
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserById(id: String): Result<List<User>> {
        return newSuspendedTransaction(db = db) {
            try {
                val result = UsersEntity.select(UsersEntity.id eq id).map {
                    User(
                        id = it[UsersEntity.id],
                        username = it[UsersEntity.username],
                        email = it[UsersEntity.email],
                        phoneNumber = it[UsersEntity.phoneNumber],
                        profilePic = it[UsersEntity.profilePic],
                        bio = it[UsersEntity.bio],
                        dateBirth = it[UsersEntity.dateBirth],
                        gender = it[UsersEntity.gender],
                        createdAt = it[UsersEntity.createdAt],
                        updatedAt = it[UsersEntity.updatedAt],
                        lastLogin = it[UsersEntity.lastLogin],
                        ipAddress = it[UsersEntity.ipAddress] ?: "can't retrieve Ip Address",
                        device = it[UsersEntity.device]
                    )
                }
                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }

        }
    }

    override suspend fun registerUser(createUserRequest: CreateUserRequest): Result<User> {
        return try {
            newSuspendedTransaction {
                val insert = UsersEntity.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(16)
                    it[username] = createUserRequest.username
                    it[email] = createUserRequest.email
                    it[password] = createUserRequest.password
                    it[phoneNumber] = createUserRequest.phoneNumber
                    it[profilePic] = createUserRequest.profilePic
                    it[bio] = createUserRequest.bio
                    it[dateBirth] = createUserRequest.dateBirth
                    it[gender] = createUserRequest.gender
                    it[createdAt] = System.currentTimeMillis()
                    it[updatedAt] = null
                    it[lastLogin] = System.currentTimeMillis()
                    it[ipAddress] = createUserRequest.ipAddress
                    it[device] = createUserRequest.device
                }
                val result = insert.resultedValues!!.map {
                    User(
                        id = it[UsersEntity.id],
                        username = it[UsersEntity.username],
                        email = it[UsersEntity.email],
                        phoneNumber = it[UsersEntity.phoneNumber],
                        profilePic = it[UsersEntity.profilePic],
                        bio = it[UsersEntity.bio],
                        dateBirth = it[UsersEntity.dateBirth],
                        gender = it[UsersEntity.gender],
                        createdAt = it[UsersEntity.createdAt],
                        updatedAt = null,
                        lastLogin = it[UsersEntity.lastLogin],
                        ipAddress = it[UsersEntity.ipAddress] ?: "can't retrieve IP Address",
                        device = it[UsersEntity.device]
                    )
                }.first()
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserLogin(
        id: String,
        ipAddress: String,
        device: String
    ): Result<Unit> {
        newSuspendedTransaction {
            UsersEntity.update({ UsersEntity.id eq id }) {
                it[UsersEntity.ipAddress] = ipAddress
                it[UsersEntity.device] = device
                it[lastLogin] = System.currentTimeMillis()
            }
        }
        return Result.success(Unit)
    }

    override suspend fun updateUserInfo(userId: String, userInfoRequest: UserInfoRequest): Result<String> {
        return newSuspendedTransaction {
            try {
                UsersEntity.update({ UsersEntity.id eq userId }) {
                    it[username] = userInfoRequest.username
                    it[email] = userInfoRequest.email
                    it[phoneNumber] = userInfoRequest.phoneNumber
                    it[profilePic] = userInfoRequest.profilePicture
                    it[bio] = userInfoRequest.bio
                    it[dateBirth] = userInfoRequest.dateBirth
                    it[gender] = userInfoRequest.gender
                }
                Result.success("Successfully updated")
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        newSuspendedTransaction {
            UsersEntity.deleteWhere { UsersEntity.id eq id }
        }
        return Result.success(Unit)
    }
}