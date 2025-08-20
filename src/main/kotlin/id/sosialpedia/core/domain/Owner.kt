package id.sosialpedia.core.domain

import kotlinx.serialization.Serializable

/**
 * @author Samuel Mareno
 * @Date 05/10/22
 */

@Serializable
data class Owner(
    val userId: String,
    val username: String,
    val profilePicUrl: String?
)
