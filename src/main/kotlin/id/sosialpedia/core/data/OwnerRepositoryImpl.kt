package id.sosialpedia.core.data

import id.sosialpedia.core.domain.Owner
import id.sosialpedia.core.domain.OwnerRepository
import id.sosialpedia.users.data.model.UserEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 06/10/22
 */
class OwnerRepositoryImpl(private val database: Database) : OwnerRepository {

    override suspend fun getOwner(userId: String): Owner {
        return newSuspendedTransaction(db = database) {
            try {
                UserEntity
                    .selectAll()
                    .where { UserEntity.id eq UUID.fromString(userId) }
                    .map {
                        Owner(
                            username = it[UserEntity.username],
                            userId = it[UserEntity.id].value.toString(),
                            profilePicUrl = it[UserEntity.profilePicUrl]
                        )
                    }.first()
            } catch (e: Exception) {
                throw IllegalArgumentException(e.message)
            }
        }
    }
}