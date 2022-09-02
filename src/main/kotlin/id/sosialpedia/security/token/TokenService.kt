package id.sosialpedia.security.token

/**
 * @author Samuel Mareno
 * @Date 30/08/22
 */
interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String
}