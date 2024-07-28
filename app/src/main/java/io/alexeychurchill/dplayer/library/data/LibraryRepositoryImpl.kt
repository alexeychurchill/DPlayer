@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.library.data

import io.alexeychurchill.dplayer.library.data.database.DirectoryDao
import io.alexeychurchill.dplayer.library.data.database.RoomLibraryRecord
import io.alexeychurchill.dplayer.library.data.database.RoomLibraryRecordMapper
import io.alexeychurchill.dplayer.library.domain.DirectoryPermissionsDispatcher
import io.alexeychurchill.dplayer.library.domain.DirectorySource
import io.alexeychurchill.dplayer.library.domain.LibraryRecord
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val directoryDao: DirectoryDao,
    private val libraryRecordMapper: RoomLibraryRecordMapper,
    private val permissionsDispatcher: DirectoryPermissionsDispatcher,
    private val mediaEntryStore: MediaEntryStore,
) : LibraryRepository {

    override val allEntries: Flow<List<MediaEntry>>
        get() = directoryDao
            .allDirectoriesByAddedDate()
            .mapLatest { roomDirs -> createDirectories(roomDirs) }

    override suspend fun getLibraryEntry(path: String): MediaEntry? {
        val roomDir = directoryDao.getDirectoryByPath(path) ?: return null
        return fetchLibraryEntry(roomDir)
    }

    override suspend fun addLibraryRecord(record: LibraryRecord) {
        permissionsDispatcher.takePermissions(record.dirPath)
        directoryDao.insertDirectory(libraryRecordMapper.mapToRoom(record))
    }

    private suspend fun createDirectories(
        roomDirs: List<RoomLibraryRecord>
    ): List<MediaEntry> = coroutineScope {
        roomDirs
            .map { roomDir -> async { fetchLibraryEntry(roomDir) } }
            .awaitAll()
    }

    private fun fetchLibraryEntry(roomRecord: RoomLibraryRecord): MediaEntry {
        return mediaEntryStore.directoryMediaEntry(roomRecord.path, sourceFrom(roomRecord))
    }

    private fun sourceFrom(roomRecord: RoomLibraryRecord): DirectorySource.FromUserLibrary {
        return DirectorySource.FromUserLibrary(
            aliasTitle = roomRecord.aliasTitle,
            createdAt = roomRecord.createdAt,
            updatedAt = roomRecord.updatedAt,
        )
    }
}
