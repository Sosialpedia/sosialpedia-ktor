package id.sosialpedia.users.routes.model

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@kotlinx.serialization.Serializable
data class CreateUserRequest(
    val id: String = "",
    val username: String,
    val email: String? = null,
    val password: String,
    val salt: String = "",
    val phoneNumber: String = "",
    val profilePic: String? = null,
    val bio: String = "",
    val dateBirth: Long = 0L,
    val gender: String,
    val lastLogin: Long? = null,
    val ipAddress: String,
    val device: String? = null
)
