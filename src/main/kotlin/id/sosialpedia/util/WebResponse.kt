package id.sosialpedia.util

import kotlinx.serialization.Serializable

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
@Serializable
data class WebResponse<T>(
    val message: String,
    val data: T,
    val code: Int
)
