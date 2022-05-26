package id.sosialpedia.chats.util

/**
 * @author Samuel Mareno
 * @Date 26/05/22
 */
class UserAlreadyExists(userId: String) : Throwable("userId: $userId is already exists")