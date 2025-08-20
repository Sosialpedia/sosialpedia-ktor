package id.sosialpedia.votes.domain.model

import kotlinx.serialization.Serializable

/**
 * Data class for receiving vote requests from the client.
 */
@Serializable
data class VoteRequest(
    val userId: String,
    // voteType: 1 for Like, -1 for Dislike
    val voteType: Short
)