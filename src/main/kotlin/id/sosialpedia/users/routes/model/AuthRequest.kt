package id.sosialpedia.users.routes.model

/**
 * @author Samuel Mareno
 * @Date 31/08/22
 */
@kotlinx.serialization.Serializable
data class AuthRequest(
    val username: String,
    val password: String
)
