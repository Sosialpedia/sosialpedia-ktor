package id.sosialpedia.users.data.model

import kotlinx.serialization.Serializable

/**
 * @author Samuel Mareno
 * @Date 02/09/22
 */

@Serializable
data class AuthResponse(
    val token: String
)