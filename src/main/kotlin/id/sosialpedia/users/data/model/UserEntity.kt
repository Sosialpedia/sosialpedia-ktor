package id.sosialpedia.users.data.model

import org.jetbrains.exposed.dao.id.UUIDTable

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
object UserEntity : UUIDTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("hashed_password", 100)
    val salt = varchar("salt", 100)
    val phoneNumber = varchar("phone_number", 50).nullable()
    val profilePicUrl = varchar("profile_pic_url", 255).nullable()
    val bio = text("bio").nullable()
    val dateOfBirth = long("date_of_birth").nullable()

    val gender = customEnumeration(
        name = "gender",
        sql = "GENDER",
        fromDb = { value ->
            Gender.valueOf(value as String)
        },
        toDb = {
            PGEnum("GENDER", it)
        }
    )

    val lastLogin = long("last_login").nullable()
    val ipAddress = varchar("ip_address", 45).nullable()
    val device = varchar("device", 150).nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at").nullable()
}