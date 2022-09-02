package id.sosialpedia.security.hashing

/**
 * @author Samuel Mareno
 * @Date 31/08/22
 */
data class SaltedHash(
    val hash: String,
    val salt: String
)
