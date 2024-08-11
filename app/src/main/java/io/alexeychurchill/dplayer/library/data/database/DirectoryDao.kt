package io.alexeychurchill.dplayer.library.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DirectoryDao {

    @Query("SELECT * FROM user_directories ORDER BY created_at DESC")
    abstract fun allDirectoriesOrderByCreatedAt(): Flow<List<RoomLibraryRecord>>

    @Query("SELECT * FROM user_directories WHERE path=:path LIMIT 1")
    abstract suspend fun getDirectoryByPath(path: String): RoomLibraryRecord?

    @Insert
    abstract suspend fun insertDirectory(record: RoomLibraryRecord)

    @Update
    abstract suspend fun updateDirectory(record: RoomLibraryRecord)
}
