package id.sosialpedia.core.data

import id.sosialpedia.core.domain.Owner
import id.sosialpedia.core.domain.OwnerRepository
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * @author Samuel Mareno
 * @Date 06/10/22
 */
class OwnerRepositoryImpl(private val database: Database) : OwnerRepository {

    override suspend fun getOwner(userId: String): Owner {
        return newSuspendedTransaction(db = database) {
            try {
                UserEntity
                    .slice(UserEntity.username, UserEntity.profilePic)
                    .select(UserEntity.id eq userId)
                    .map {
                        Owner(
                            username = it[UserEntity.username],
                            profilePic = it[UserEntity.profilePic]
                        )
                    }.first()
            } catch (e: Exception) {
                throw IllegalArgumentException(e.message)
            }
        }
    }
}