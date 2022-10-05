package id.sosialpedia.users.data.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
object UsersEntity : Table("users") {
    val id: Column<String> = varchar("id", 50).uniqueIndex()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 100).nullable().uniqueIndex()
    val password = varchar("hashed_password", 100)
    val salt = varchar("salt", 100)
    val phoneNumber = varchar("phone_number", 50).nullable()
    val bio = text("bio").nullable()
    val dateBirth = long("date_birth").nullable()
    val createdAt = long("created_at")
    val updatedAt = long("updated_at").nullable()
    val lastLogin = long("last_login").nullable()
    val ipAddress = varchar("ip_address", 45).nullable()
    val device = varchar("device", 150).nullable()
    val profilePic = varchar("profile_picture", 100).nullable()
    val gender = customEnumeration(
        name = "gender",
        sql = "Gender",
        fromDb = { value ->
            Gender.valueOf(value as String)
        }, toDb = { gender ->
            PGEnum("Gender", gender)
        })

    override val primaryKey = PrimaryKey(id)
}