@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.core.data.filesystem.FilesExtensions
import io.alexeychurchill.clown.core.data.filesystem.FilesystemStore
import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.clown.core.utils.fileExtension
import io.alexeychurchill.clown.library.data.database.DirectoryDao
import io.alexeychurchill.clown.library.data.database.RoomLibraryRecord
import io.alexeychurchill.clown.library.data.database.RoomLibraryRecordMapper
import io.alexeychurchill.clown.library.domain.DirectoryPermissionsDispatcher
import io.alexeychurchill.clown.library.domain.DirectorySource
import io.alexeychurchill.clown.library.domain.LibraryEntry
import io.alexeychurchill.clown.library.domain.LibraryRecord
import io.alexeychurchill.clown.library.domain.LibraryRepository
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
    private val filesystemStore: FilesystemStore,
) : LibraryRepository {

    override val allEntries: Flow<List<LibraryEntry>>
        get() = directoryDao
            .allDirectoriesByAddedDate()
            .mapLatest { roomDirs -> createDirectories(roomDirs) }

    override suspend fun getLibraryEntry(path: String): LibraryEntry? {
        val roomDir = directoryDao.getDirectoryByPath(path) ?: return null
        return fetchLibraryEntry(roomDir)
    }

    override suspend fun addLibraryRecord(record: LibraryRecord) {
        permissionsDispatcher.takePermissions(record.dirPath)
        directoryDao.insertDirectory(libraryRecordMapper.mapToRoom(record))
    }

    private suspend fun createDirectories(
        roomDirs: List<RoomLibraryRecord>
    ): List<LibraryEntry> = coroutineScope {
        roomDirs
            .map { roomDir -> async { fetchLibraryEntry(roomDir) } }
            .awaitAll()
    }

    private fun fetchLibraryEntry(roomDir: RoomLibraryRecord): LibraryEntry {
        val childEntries = filesystemStore.list(roomDir.path)
        val musicFileCount = childEntries.count { entry ->
            val ext = ((entry as? FileSystemEntry.File)?.name as? FileName.Name)
                ?.value
                ?.fileExtension ?: ""
            FilesExtensions.MusicFiles.contains(ext)
        }
        val directoryCount = childEntries.count { entry -> entry is FileSystemEntry.Directory }
        return LibraryEntry.Directory(
            directoryEntry = filesystemStore.directoryBy(roomDir.path),
            musicFileCount = musicFileCount,
            subDirectoryCount = directoryCount,
            source = DirectorySource.FromUserLibrary(
                aliasTitle = roomDir.aliasTitle,
                createdAt = roomDir.createdAt,
                updatedAt = roomDir.updatedAt,
            ),
        )
    }
}
