package id.sosialpedia.chats.rooms.domain.model

/**
 * @author Samuel Mareno
 * @Date 25/05/22
 */
@kotlinx.serialization.Serializable
data class RoomWithMembers(
    val roomId: String,
    val usersId: MutableList<String> = mutableListOf(),
    val usernames: MutableList<String> = mutableListOf()
)
