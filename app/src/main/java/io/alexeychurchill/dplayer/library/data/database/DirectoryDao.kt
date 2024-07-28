package io.alexeychurchill.dplayer.library.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DirectoryDao {

    @Query("SELECT * FROM user_directories ORDER BY created_at DESC")
    abstract fun allDirectoriesByAddedDate(): Flow<List<RoomLibraryRecord>>

    @Query("SELECT * FROM user_directories WHERE path=:path LIMIT 1")
    abstract suspend fun getDirectoryByPath(path: String): RoomLibraryRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertDirectory(directory: RoomLibraryRecord)
}
