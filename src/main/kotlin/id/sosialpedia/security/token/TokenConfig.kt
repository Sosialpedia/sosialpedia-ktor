package id.sosialpedia.security.token

/**
 * @author Samuel Mareno
 * @Date 30/08/22
 */
data class TokenConfig(
    val issuer: String,
    val audience: String,
    val expiresIn: Long,
    val secret: String
)