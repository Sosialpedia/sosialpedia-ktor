package id.sosialpedia.core.data

import id.sosialpedia.core.domain.Owner
import id.sosialpedia.core.domain.OwnerRepository
import id.sosialpedia.users.data.model.UsersEntity
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
                UsersEntity
                    .slice(UsersEntity.username, UsersEntity.profilePic)
                    .select(UsersEntity.id eq userId)
                    .map {
                        Owner(
                            username = it[UsersEntity.username],
                            profilePic = it[UsersEntity.profilePic]
                        )
                    }.first()
            } catch (e: Exception) {
                throw IllegalArgumentException(e.message)
            }
        }
    }
}