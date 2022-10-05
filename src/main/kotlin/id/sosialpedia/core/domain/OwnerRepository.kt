package id.sosialpedia.core.domain

/**
 * @author Samuel Mareno
 * @Date 06/10/22
 */
interface OwnerRepository {
    suspend fun getOwner(userId: String): Owner
}