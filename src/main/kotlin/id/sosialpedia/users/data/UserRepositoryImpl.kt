package id.sosialpedia.users.data

import id.sosialpedia.users.data.model.Gender
import id.sosialpedia.users.data.model.UsersEntity
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.domain.model.User
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.users.routes.model.UserInfoRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
                        password = it[UsersEntity.password],
                        salt = it[UsersEntity.salt],
                        profilePic = it[UsersEntity.profilePic],
                        bio = it[UsersEntity.bio],
                        dateBirth = it[UsersEntity.dateBirth],
                        gender = it[UsersEntity.gender].name,
                        createdAt = it[UsersEntity.createdAt],
                        updatedAt = it[UsersEntity.updatedAt],
                        lastLogin = it[UsersEntity.lastLogin],
                        ipAddress = it[UsersEntity.ipAddress],
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

    override suspend fun getUserById(userId: String): User? {
        return newSuspendedTransaction(db = db) {
            try {
                val users = UsersEntity.select(UsersEntity.id eq userId).map {
                    User(
                        id = it[UsersEntity.id],
                        username = it[UsersEntity.username],
                        email = it[UsersEntity.email],
                        phoneNumber = it[UsersEntity.phoneNumber],
                        password = it[UsersEntity.password],
                        salt = it[UsersEntity.salt],
                        profilePic = it[UsersEntity.profilePic],
                        bio = it[UsersEntity.bio],
                        dateBirth = it[UsersEntity.dateBirth],
                        gender = it[UsersEntity.gender].name,
                        createdAt = it[UsersEntity.createdAt],
                        updatedAt = it[UsersEntity.updatedAt],
                        lastLogin = it[UsersEntity.lastLogin],
                        ipAddress = it[UsersEntity.ipAddress] ?: "can't retrieve the Ip Address",
                        device = it[UsersEntity.device]
                    )
                }
                users[0]
            } catch (e: Exception) {
                null
            }

        }
    }

    override suspend fun getUserByUsername(username: String): User? {
        return newSuspendedTransaction {
            try {
                val resultRow = UsersEntity
                    .select(UsersEntity.username eq username).firstOrNull()
                if (resultRow == null) {
                    null
                } else {
                    User(
                        id = resultRow[UsersEntity.id],
                        username = resultRow[UsersEntity.username],
                        email = resultRow[UsersEntity.email],
                        phoneNumber = resultRow[UsersEntity.phoneNumber],
                        password = resultRow[UsersEntity.password],
                        salt = resultRow[UsersEntity.salt],
                        profilePic = resultRow[UsersEntity.profilePic],
                        bio = resultRow[UsersEntity.bio],
                        dateBirth = resultRow[UsersEntity.dateBirth],
                        gender = resultRow[UsersEntity.gender].name,
                        createdAt = resultRow[UsersEntity.createdAt],
                        updatedAt = resultRow[UsersEntity.updatedAt],
                        lastLogin = resultRow[UsersEntity.lastLogin],
                        ipAddress = resultRow[UsersEntity.ipAddress] ?: "can't retrieve Ip Address",
                        device = resultRow[UsersEntity.device]
                    )
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    override suspend fun registerUser(createUserRequest: CreateUserRequest): Result<User> {
        return try {
            newSuspendedTransaction {
                val insert = UsersEntity.insert { it ->
                    it[username] = createUserRequest.username
                    it[email] = createUserRequest.email
                    it[password] = createUserRequest.password
                    it[salt] = createUserRequest.salt
                    it[phoneNumber] = createUserRequest.phoneNumber
                    it[profilePic] = createUserRequest.profilePic
                    it[bio] = createUserRequest.bio
                    it[dateBirth] = createUserRequest.dateBirth
                    it[gender] = Gender.valueOf(createUserRequest.gender)
                    it[createdAt] = System.currentTimeMillis()
                    it[updatedAt] = null
                    it[lastLogin] = System.currentTimeMillis()
                    it[ipAddress] = createUserRequest.ipAddress
                    it[device] = createUserRequest.device
                }
                val result = insert.resultedValues!!.map {
                    User(
                        id = "secretId",
                        username = it[UsersEntity.username],
                        email = it[UsersEntity.email],
                        password = it[UsersEntity.password],
                        salt = it[UsersEntity.salt],
                        phoneNumber = it[UsersEntity.phoneNumber],
                        profilePic = it[UsersEntity.profilePic],
                        bio = it[UsersEntity.bio],
                        dateBirth = it[UsersEntity.dateBirth],
                        gender = it[UsersEntity.gender].name,
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
                    it[gender] = Gender.valueOf(userInfoRequest.gender)
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