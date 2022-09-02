package id.sosialpedia.users.data

/**
 * @author Samuel Mareno
 * @Date 02/09/22
 */

@kotlinx.serialization.Serializable
data class AuthResponse(
    val token: String
)
