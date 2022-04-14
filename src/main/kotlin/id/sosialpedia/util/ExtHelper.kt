package id.sosialpedia.util

import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author Samuel Mareno
 * @Date 12/04/22
 */
fun UUID.toShuffledMD5(length: Int): String {
    val md = MessageDigest.getInstance("MD5")
    val chars = BigInteger(1, md.digest(toString().toByteArray()))
        .toString(16)
        .padStart(32, '0')
        .take(length)
        .toCharArray()
        .also {
            it.shuffle()
        }
    return String(chars)
}

fun LocalDateTime.toMills(): Long {
    return atZone(ZoneId.of("Asia/Jakarta"))
        .toInstant()
        .toEpochMilli()
}

fun LocalDateTime.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    return format(formatter)
}

fun String.toLocalDateTime(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    return LocalDateTime.parse(this, formatter)
}

fun <T : Any> String.execAndMap(transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    TransactionManager.current().exec(this) { rs ->
        while (rs.next()) {
            result += transform(rs)
        }
    }
    return result
}
