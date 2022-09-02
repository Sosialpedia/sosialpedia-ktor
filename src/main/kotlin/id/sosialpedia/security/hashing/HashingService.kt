package id.sosialpedia.security.hashing

/**
 * @author Samuel Mareno
 * @Date 31/08/22
 */
interface HashingService {
    fun generateSaltedHash(password: String, saltLength: Int = 32): SaltedHash
    fun verify(password: String, saltedHash: SaltedHash): Boolean
}