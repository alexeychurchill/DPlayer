package io.alexeychurchill.clown.library.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DirectoryDao {

    @Query("SELECT * FROM user_directories ORDER BY created_at DESC")
    abstract fun allDirectoriesByAddedDate(): Flow<List<RoomLibraryEntry>>

    @Query("SELECT * FROM user_directories WHERE path=:path LIMIT 1")
    abstract suspend fun getDirectoryByPath(path: String): RoomLibraryEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDirectory(directory: RoomLibraryEntry)
}
