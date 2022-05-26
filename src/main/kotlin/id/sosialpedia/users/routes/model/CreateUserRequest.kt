package id.sosialpedia.users.routes.model

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@kotlinx.serialization.Serializable
data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val profilePic: String? = null,
    val bio: String = "",
    val dateBirth: Long,
    val gender: String,
    val lastLogin: Long? = null,
    val ipAddress: String,
    val device: String? = null
)
