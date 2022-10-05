package id.sosialpedia.users.domain.model

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@kotlinx.serialization.Serializable
data class User(
    val id: String,
    val username: String,
    val email: String?,
    val password: String,
    val salt: String,
    val phoneNumber: String?,
    val profilePic: String?,
    val bio: String?,
    val dateBirth: Long?,
    val gender: String,
    val createdAt: Long,
    val updatedAt: Long?,
    val lastLogin: Long?,
    val ipAddress: String?,
    val device: String?
)
