package id.sosialpedia.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

/**
 * @author Samuel Mareno
 * @Date 31/08/22
 */
class SHA256HashingService : HashingService {

    override fun generateSaltedHash(password: String, saltLength: Int): SaltedHash {
        val saltHex = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val salt = Hex.encodeHexString(saltHex)
        val hash = DigestUtils.sha256Hex("$salt$password")
        println("salt: $salt")
        println("hash: $hash")
        return SaltedHash(
            hash = hash,
            salt = salt
        )
    }

    override fun verify(password: String, saltedHash: SaltedHash): Boolean {
        println("verify_password: $password")
        println("verify_salt: ${saltedHash.salt}")
        println("verify_hash: ${saltedHash.hash}")
        return DigestUtils.sha256Hex(saltedHash.salt + password) == saltedHash.hash
    }
}