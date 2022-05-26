package id.sosialpedia.users.routes.model

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */

@kotlinx.serialization.Serializable
data class UserInfoRequest(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val profilePicture: String,
    val bio: String,
    val dateBirth: Long,
    val gender: String
)
