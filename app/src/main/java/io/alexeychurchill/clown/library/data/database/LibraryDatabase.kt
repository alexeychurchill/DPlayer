package io.alexeychurchill.clown.library.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.alexeychurchill.clown.core.data.database.LocalDateTimeTypeConverter

@TypeConverters(
    value = [
        LocalDateTimeTypeConverter::class,
    ]
)
@Database(
    version = 1,
    entities = [
        RoomDirectory::class,
    ]
)
abstract class LibraryDatabase : RoomDatabase() {

    companion object {
        const val NAME = "user_library.db"
    }

    abstract fun directoryDao(): DirectoryDao
}
