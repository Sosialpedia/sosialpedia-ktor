package id.sosialpedia.chats.util

/**
 * @author Samuel Mareno
 * @Date 25/05/22
 */
class UserNotInRoom(userId: String) : Throwable("$userId not in this room")