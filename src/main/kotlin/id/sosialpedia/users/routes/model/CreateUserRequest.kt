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
    val salt: String = "",
    val phoneNumber: String? = null,
    val profilePic: String? = null,
    val bio: String? = null,
    val dateBirth: Long = 0L,
    val gender: String = "NotSpecified",
    val lastLogin: Long? = null,
    val ipAddress: String,
    val device: String? = null
)
