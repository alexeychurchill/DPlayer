@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.library.data

import io.alexeychurchill.dplayer.core.domain.time.DateTimeProvider
import io.alexeychurchill.dplayer.library.data.database.DirectoryDao
import io.alexeychurchill.dplayer.library.data.database.RoomLibraryRecord
import io.alexeychurchill.dplayer.library.data.database.RoomLibraryRecordMapper
import io.alexeychurchill.dplayer.library.domain.DirectoryPermissionsDispatcher
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.LibraryRecord
import io.alexeychurchill.dplayer.library.domain.LibraryRepository
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LibraryRepositoryImpl @Inject constructor(
    private val directoryDao: DirectoryDao,
    private val permissionsDispatcher: DirectoryPermissionsDispatcher,
    private val dateTimeProvider: DateTimeProvider,
    private val mediaEntryStore: MediaEntryStore,
    private val libraryRecordMapper: RoomLibraryRecordMapper,
) : LibraryRepository {

    override val allEntries: Flow<List<MediaEntry>>
        get() = directoryDao
            .allDirectoriesOrderByCreatedAt()
            .mapLatest { roomDirs -> createDirectories(roomDirs) }

    override suspend fun getLibraryEntry(path: String): MediaEntry? {
        val roomDir = directoryDao.getDirectoryByPath(path) ?: return null
        return withContext(Dispatchers.IO) { fetchLibraryEntry(roomDir) }
    }

    override suspend fun setDirectoryAlias(uri: String, aliasName: String?) {
        val libraryRecord = directoryDao.getDirectoryByPath(uri)
            ?: throw IllegalArgumentException("No $uri directory!")

        val updated = libraryRecord.copy(
            aliasTitle = aliasName,
            updatedAt = dateTimeProvider.current(),
        )
        directoryDao.updateDirectory(updated)
    }

    override suspend fun putLibraryRecord(record: LibraryRecord) {
        permissionsDispatcher.takePermissions(record.dirPath)
        directoryDao.insertDirectory(libraryRecordMapper.mapToRoom(record))
    }

    override suspend fun removeEntry(path: String) {
        permissionsDispatcher.releasePermissions(path)
        directoryDao.removeDirectory(path)
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

    private fun sourceFrom(roomRecord: RoomLibraryRecord): EntrySource.UserLibrary {
        return EntrySource.UserLibrary(
            aliasTitle = roomRecord.aliasTitle,
            createdAt = roomRecord.createdAt,
            updatedAt = roomRecord.updatedAt,
        )
    }
}
