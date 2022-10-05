package id.sosialpedia.users.data.model

import org.postgresql.util.PGobject

/**
 * @author Samuel Mareno
 * @Date 05/10/22
 */

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}