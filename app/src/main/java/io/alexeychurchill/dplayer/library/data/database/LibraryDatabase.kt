package io.alexeychurchill.dplayer.library.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.alexeychurchill.dplayer.core.data.database.LocalDateTimeTypeConverter

@TypeConverters(
    value = [
        LocalDateTimeTypeConverter::class,
    ]
)
@Database(
    version = 1,
    entities = [
        RoomLibraryRecord::class,
    ]
)
abstract class LibraryDatabase : RoomDatabase() {

    companion object {
        const val NAME = "user_library.db"
    }

    abstract fun directoryDao(): DirectoryDao
}
