package id.sosialpedia.users.domain.model

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@kotlinx.serialization.Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val profilePic: String? = null,
    val bio: String = "",
    val dateBirth: String,
    val gender: String,
    val createdAt: String,
    val updatedAt: String? = null,
    val lastLogin: String?,
    val ipAddress: String,
    val device: String? = null
)
