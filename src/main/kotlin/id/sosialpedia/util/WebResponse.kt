package id.sosialpedia.util

import kotlinx.serialization.Serializable

@Serializable
data class WebResponse<T>(
    val message: String,
    val data: T,
    val code: Int
)
