package id.sosialpedia.users.data

import id.sosialpedia.users.data.model.Users
import id.sosialpedia.users.routes.model.UserInfoRequest
import id.sosialpedia.util.toShuffledMD5
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.util.*

class UserRepositoryImpl(private val db: Database) : UserRepository {
    override suspend fun getAllUsers(): List<String> {
        return newSuspendedTransaction {
            Users.selectAll().map { resultRow ->
                resultRow[Users.username]
            }
        }
    }

    override suspend fun getUserById(id: String): String {
        return newSuspendedTransaction(db = db) {
            Users.select(Users.id eq id).first()[Users.id]
        }
    }

    override suspend fun registerUser(): Result<Unit> {
        return try {
            newSuspendedTransaction {
                Users.insert {
                    it[id] = UUID.randomUUID().toShuffledMD5(16)
                    it[username] = "bib"
                    it[email] = "b3b2@gmail.com"
                    it[password] = "hashedPassword"
                    it[phoneNumber] = "081763f6373"
                    it[bio] = "my bio"
                    it[dateBirth] = LocalDateTime.parse("2002-12-03T15:20:30")
                    it[gender] = "Not Specified"
                    it[createdAt] = LocalDateTime.now()
                    it[updatedAt] = null
                    it[lastLogin] = null
                    it[ipAddress] = null
                    it[device] = null
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is ExposedSQLException -> {
                    Result.failure(e)
                }
                else -> Result.failure(Throwable("Unknown error occurred"))
            }
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

    override suspend fun updateUserInfo(userInfoRequest: UserInfoRequest): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        newSuspendedTransaction {
            Users.deleteWhere { Users.id eq id }
        }
        return Result.success(Unit)
    }
}