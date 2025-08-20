package id.sosialpedia.users.domain.model

import kotlinx.serialization.Serializable

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@Serializable
data class RegisteredUser(
    val id: String,
    val username: String,
    val email: String?,
    val phoneNumber: String?,
    val profilePic: String?,
    val bio: String?,
    val dateBirth: Long?,
    val gender: String,
)
