package id.sosialpedia.users.data

import id.sosialpedia.users.data.model.Users
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.domain.model.User
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.users.routes.model.UserInfoRequest
import id.sosialpedia.util.toFormattedString
import id.sosialpedia.util.toLocalDateTime
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
class UserRepositoryImpl(private val db: Database) : UserRepository {
    override suspend fun getAllUsers(): List<User> {
        return newSuspendedTransaction {
            Users.selectAll().map {
                User(
                    id = it[Users.id],
                    username = it[Users.username],
                    email = it[Users.email],
                    phoneNumber = it[Users.phoneNumber],
                    profilePic = it[Users.profilePic],
                    bio = it[Users.bio],
                    dateBirth = it[Users.dateBirth].toFormattedString(),
                    gender = it[Users.gender],
                    createdAt = it[Users.createdAt].toFormattedString(),
                    updatedAt = it[Users.updatedAt]?.toFormattedString(),
                    lastLogin = it[Users.lastLogin]?.toFormattedString(),
                    ipAddress = it[Users.ipAddress] ?: "can't retrieve Ip Address",
                    device = it[Users.device]
                )
            }
        }
    }

    override suspend fun getUserById(id: String): Result<List<User>> {
        return newSuspendedTransaction(db = db) {
            try {
                val result = Users.select(Users.id eq id).map {
                    User(
                        id = it[Users.id],
                        username = it[Users.username],
                        email = it[Users.email],
                        phoneNumber = it[Users.phoneNumber],
                        profilePic = it[Users.profilePic],
                        bio = it[Users.bio],
                        dateBirth = it[Users.dateBirth].toFormattedString(),
                        gender = it[Users.gender],
                        createdAt = it[Users.createdAt].toFormattedString(),
                        updatedAt = it[Users.updatedAt]?.toFormattedString(),
                        lastLogin = it[Users.lastLogin]?.toFormattedString(),
                        ipAddress = it[Users.ipAddress] ?: "can't retrieve Ip Address",
                        device = it[Users.device]
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
                val insert = Users.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(16)
                    it[username] = createUserRequest.username
                    it[email] = createUserRequest.email
                    it[password] = createUserRequest.password
                    it[phoneNumber] = createUserRequest.phoneNumber
                    it[profilePic] = createUserRequest.profilePic
                    it[bio] = createUserRequest.bio
                    it[dateBirth] = createUserRequest.dateBirth.toLocalDateTime()
                    it[gender] = createUserRequest.gender
                    it[createdAt] = LocalDateTime.now()
                    it[updatedAt] = null
                    it[lastLogin] = LocalDateTime.now()
                    it[ipAddress] = createUserRequest.ipAddress
                    it[device] = createUserRequest.device
                }
                val result = insert.resultedValues!!.map {
                    User(
                        id = it[Users.id],
                        username = it[Users.username],
                        email = it[Users.email],
                        phoneNumber = it[Users.phoneNumber],
                        profilePic = it[Users.profilePic],
                        bio = it[Users.bio],
                        dateBirth = it[Users.dateBirth].toFormattedString(),
                        gender = it[Users.gender],
                        createdAt = it[Users.createdAt].toFormattedString(),
                        updatedAt = null,
                        lastLogin = it[Users.lastLogin]?.toFormattedString(),
                        ipAddress = it[Users.ipAddress] ?: "can't retrieve Ip Address",
                        device = it[Users.device]
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
            Users.update({ Users.id eq id }) {
                it[Users.ipAddress] = ipAddress
                it[Users.device] = device
                it[lastLogin] = LocalDateTime.now()
            }
        }
        return Result.success(Unit)
    }

    override suspend fun updateUserInfo(userId: String, userInfoRequest: UserInfoRequest): Result<String> {
        return newSuspendedTransaction {
            try {
                Users.update({ Users.id eq userId }) {
                    it[username] = userInfoRequest.username
                    it[email] = userInfoRequest.email
                    it[phoneNumber] = userInfoRequest.phoneNumber
                    it[profilePic] = userInfoRequest.profilePicture
                    it[bio] = userInfoRequest.bio
                    it[dateBirth] = userInfoRequest.dateBirth.toLocalDateTime()
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
            Users.deleteWhere { Users.id eq id }
        }
        return Result.success(Unit)
    }
}