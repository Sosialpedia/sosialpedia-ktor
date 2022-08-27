package id.sosialpedia.users.routes.model

import id.sosialpedia.util.toShuffledMD5
import java.util.UUID

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@kotlinx.serialization.Serializable
data class CreateUserRequest(
    val id: String = UUID.randomUUID().toShuffledMD5(16),
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
