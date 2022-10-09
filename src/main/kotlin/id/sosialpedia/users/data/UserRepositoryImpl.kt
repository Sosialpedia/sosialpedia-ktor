package id.sosialpedia.users.data

import id.sosialpedia.users.data.model.Gender
import id.sosialpedia.users.data.model.UserEntity
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
                val result = UserEntity.selectAll().map {
                    User(
                        id = it[UserEntity.id],
                        username = it[UserEntity.username],
                        email = it[UserEntity.email],
                        phoneNumber = it[UserEntity.phoneNumber],
                        password = it[UserEntity.password],
                        salt = it[UserEntity.salt],
                        profilePic = it[UserEntity.profilePic],
                        bio = it[UserEntity.bio],
                        dateBirth = it[UserEntity.dateBirth],
                        gender = it[UserEntity.gender].name,
                        createdAt = it[UserEntity.createdAt],
                        updatedAt = it[UserEntity.updatedAt],
                        lastLogin = it[UserEntity.lastLogin],
                        ipAddress = it[UserEntity.ipAddress],
                        device = it[UserEntity.device]
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
                val users = UserEntity.select(UserEntity.id eq userId).map {
                    User(
                        id = it[UserEntity.id],
                        username = it[UserEntity.username],
                        email = it[UserEntity.email],
                        phoneNumber = it[UserEntity.phoneNumber],
                        password = it[UserEntity.password],
                        salt = it[UserEntity.salt],
                        profilePic = it[UserEntity.profilePic],
                        bio = it[UserEntity.bio],
                        dateBirth = it[UserEntity.dateBirth],
                        gender = it[UserEntity.gender].name,
                        createdAt = it[UserEntity.createdAt],
                        updatedAt = it[UserEntity.updatedAt],
                        lastLogin = it[UserEntity.lastLogin],
                        ipAddress = it[UserEntity.ipAddress] ?: "can't retrieve the Ip Address",
                        device = it[UserEntity.device]
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
                val resultRow = UserEntity
                    .select(UserEntity.username eq username).firstOrNull()
                if (resultRow == null) {
                    null
                } else {
                    User(
                        id = resultRow[UserEntity.id],
                        username = resultRow[UserEntity.username],
                        email = resultRow[UserEntity.email],
                        phoneNumber = resultRow[UserEntity.phoneNumber],
                        password = resultRow[UserEntity.password],
                        salt = resultRow[UserEntity.salt],
                        profilePic = resultRow[UserEntity.profilePic],
                        bio = resultRow[UserEntity.bio],
                        dateBirth = resultRow[UserEntity.dateBirth],
                        gender = resultRow[UserEntity.gender].name,
                        createdAt = resultRow[UserEntity.createdAt],
                        updatedAt = resultRow[UserEntity.updatedAt],
                        lastLogin = resultRow[UserEntity.lastLogin],
                        ipAddress = resultRow[UserEntity.ipAddress] ?: "can't retrieve Ip Address",
                        device = resultRow[UserEntity.device]
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
                val insert = UserEntity.insert { it ->
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
                        username = it[UserEntity.username],
                        email = it[UserEntity.email],
                        password = it[UserEntity.password],
                        salt = it[UserEntity.salt],
                        phoneNumber = it[UserEntity.phoneNumber],
                        profilePic = it[UserEntity.profilePic],
                        bio = it[UserEntity.bio],
                        dateBirth = it[UserEntity.dateBirth],
                        gender = it[UserEntity.gender].name,
                        createdAt = it[UserEntity.createdAt],
                        updatedAt = null,
                        lastLogin = it[UserEntity.lastLogin],
                        ipAddress = it[UserEntity.ipAddress] ?: "can't retrieve IP Address",
                        device = it[UserEntity.device]
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
            UserEntity.update({ UserEntity.id eq id }) {
                it[UserEntity.ipAddress] = ipAddress
                it[UserEntity.device] = device
                it[lastLogin] = System.currentTimeMillis()
            }
        }
        return Result.success(Unit)
    }

    override suspend fun updateUserInfo(userId: String, userInfoRequest: UserInfoRequest): Result<String> {
        return newSuspendedTransaction {
            try {
                UserEntity.update({ UserEntity.id eq userId }) {
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
            UserEntity.deleteWhere { UserEntity.id eq id }
        }
        return Result.success(Unit)
    }
}