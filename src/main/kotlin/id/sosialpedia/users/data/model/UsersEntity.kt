package id.sosialpedia.users.data.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
object UsersEntity : Table("users") {
    val id: Column<String> = varchar("id", 16).uniqueIndex()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val password = varchar("password", 50)
    val phoneNumber = varchar("phone_number", 50)
    val bio = text("bio").nullable()
    val dateBirth = long("date_birth")
    val gender = varchar("gender", 14)
    val createdAt = long("created_at")
    val updatedAt = long("updated_at").nullable()
    val lastLogin = long("last_login").nullable()
    val ipAddress = varchar("ip_address", 45).nullable()
    val device = varchar("device", 150).nullable()
    val profilePic = varchar("profile_picture", 100).nullable()

    override val primaryKey = PrimaryKey(id)
}