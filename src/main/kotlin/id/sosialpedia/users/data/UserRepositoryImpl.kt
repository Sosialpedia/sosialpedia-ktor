package id.sosialpedia.users.data

import id.sosialpedia.users.data.model.Gender
import id.sosialpedia.users.data.model.UserEntity
import id.sosialpedia.users.domain.UserRepository
import id.sosialpedia.users.domain.model.RegisteredUser
import id.sosialpedia.users.domain.model.User
import id.sosialpedia.users.routes.model.CreateUserRequest
import id.sosialpedia.users.routes.model.UserInfoRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 08/08/2025
 */
class UserRepositoryImpl(private val db: Database) : UserRepository {

    private fun toUser(row: ResultRow) = User(
        id = row[UserEntity.id].value.toString(),
        username = row[UserEntity.username],
        email = row[UserEntity.email],
        phoneNumber = row[UserEntity.phoneNumber],
        password = row[UserEntity.password],
        salt = row[UserEntity.salt],
        profilePic = row[UserEntity.profilePicUrl],
        bio = row[UserEntity.bio],
        dateBirth = row[UserEntity.dateOfBirth],
        gender = row[UserEntity.gender].name,
        createdAt = row[UserEntity.createdAt],
        updatedAt = row[UserEntity.updatedAt],
        lastLogin = row[UserEntity.lastLogin],
        ipAddress = row[UserEntity.ipAddress],
        device = row[UserEntity.device]
    )

    private fun toRegisteredUser(row: ResultRow) = RegisteredUser(
        id = "*****" + row[UserEntity.id].value.toString().substring(row[UserEntity.id].value.toString().length - 4),
        username = row[UserEntity.username],
        email = row[UserEntity.email],
        phoneNumber = row[UserEntity.phoneNumber],
        profilePic = row[UserEntity.profilePicUrl],
        bio = row[UserEntity.bio],
        dateBirth = row[UserEntity.dateOfBirth],
        gender = row[UserEntity.gender].name
    )

    override suspend fun getAllUsers(): Result<List<User>> {
        return newSuspendedTransaction {
            try {
                val result = UserEntity.selectAll().map(::toUser)
                Result.success(result)
            } catch (e: Exception) {
                println("error getUser $e")
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserById(userId: String): Result<User> {
        return newSuspendedTransaction(db = db) {
            try {
                val user = UserEntity.selectAll().where { UserEntity.id eq UUID.fromString(userId) }
                    .map(::toUser)
                    .firstOrNull() ?: throw NoSuchElementException("User with ID $userId not found")
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserByUsername(username: String): Result<User> {
        return newSuspendedTransaction {
            try {
                val user = UserEntity.selectAll().where { UserEntity.username eq username }
                    .map(::toUser)
                    .firstOrNull() ?: throw NoSuchElementException("User with username $username not found")
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun getUserByEmail(email: String): Result<User> {
        return newSuspendedTransaction {
            try {
                val user = UserEntity.selectAll().where { UserEntity.email eq email }
                    .map(::toUser)
                    .firstOrNull() ?: throw NoSuchElementException("User with email $email not found")
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun registerUser(createUserRequest: CreateUserRequest): Result<RegisteredUser> {
        return newSuspendedTransaction {
            try {
                val insertStatement = UserEntity.insert {
                    it[username] = createUserRequest.username
                    it[email] = createUserRequest.email
                    it[password] = createUserRequest.password
                    it[salt] = createUserRequest.salt
                    it[phoneNumber] = createUserRequest.phoneNumber
                    it[profilePicUrl] = createUserRequest.profilePic
                    it[bio] = createUserRequest.bio
                    it[dateOfBirth] = createUserRequest.dateBirth
                    it[gender] = Gender.valueOf(createUserRequest.gender)
                    it[createdAt] = System.currentTimeMillis()
                    it[updatedAt] = null
                    it[lastLogin] = System.currentTimeMillis()
                    it[ipAddress] = createUserRequest.ipAddress
                    it[device] = createUserRequest.device
                }
                val newUser = insertStatement.resultedValues!!.map(::toRegisteredUser).first()
                Result.success(newUser)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateUserLogin(
        id: String,
        ipAddress: String,
        device: String
    ): Result<Unit> {
        newSuspendedTransaction {
            UserEntity.update({ UserEntity.id eq UUID.fromString(id) }) {
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
                UserEntity.update({ UserEntity.id eq UUID.fromString(userId) }) {
                    it[username] = userInfoRequest.username
                    it[email] = userInfoRequest.email
                    it[phoneNumber] = userInfoRequest.phoneNumber
                    it[profilePicUrl] = userInfoRequest.profilePicture
                    it[bio] = userInfoRequest.bio
                    it[dateOfBirth] = userInfoRequest.dateBirth
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
            UserEntity.deleteWhere { UserEntity.id eq UUID.fromString(id) }
        }
        return Result.success(Unit)
    }
}