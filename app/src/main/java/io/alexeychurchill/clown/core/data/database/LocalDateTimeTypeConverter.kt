package io.alexeychurchill.clown.core.data.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object LocalDateTimeTypeConverter {

    @TypeConverter
    fun toString(dateTime: LocalDateTime): String {
        val instant = dateTime.toInstant(ZoneOffset.UTC)
        return try {
            DateTimeFormatter.ISO_INSTANT.format(instant)
        } catch (ignored: Throwable) {
            // TODO: [LOGGING] ignored
            ""
        }
    }

    @TypeConverter
    fun fromString(value: String): LocalDateTime {
        return try {
            val instant = Instant.parse(value)
            LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
        } catch (ignored: Throwable) {
            // TODO: [LOGGING]: ignored
            LocalDateTime.MIN
        }
    }
}
