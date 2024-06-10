@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.core.data.filesystem.FilesystemStore
import io.alexeychurchill.clown.library.data.database.DirectoryDao
import io.alexeychurchill.clown.library.data.database.RoomLibraryEntry
import io.alexeychurchill.clown.library.data.database.RoomLibraryEntryMapper
import io.alexeychurchill.clown.library.domain.DirectoryPermissionsDispatcher
import io.alexeychurchill.clown.library.domain.LibraryEntry
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
    private val mapper: RoomLibraryEntryMapper,
    private val permissionsDispatcher: DirectoryPermissionsDispatcher,
    private val filesystemStore: FilesystemStore,
) : LibraryRepository {

    override val allDirectories: Flow<List<LibraryEntry>>
        get() = directoryDao
            .allDirectoriesByAddedDate()
            .mapLatest { roomDirs -> createDirectories(roomDirs) }

    override suspend fun getDirectory(path: String): LibraryEntry? {
        return directoryDao.getDirectoryByPath(path)?.let { roomDir ->
            LibraryEntry(
                directory = filesystemStore.directoryBy(roomDir.path),
                aliasTitle = roomDir.aliasTitle,
                createdAt = roomDir.createdAt,
                updatedAt = roomDir.updatedAt,
            )
        }
    }

    override suspend fun addDirectory(entry: LibraryEntry) {
        entry.directory?.path?.let { uri ->
            permissionsDispatcher.takePermissions(uri)
        }
        directoryDao.insertDirectory(mapper.mapToRoom(entry))
    }

    private suspend fun createDirectories(
        roomDirs: List<RoomLibraryEntry>
    ): List<LibraryEntry> = coroutineScope {
        roomDirs
            .map { roomDir ->
                async {
                    LibraryEntry(
                        directory = filesystemStore.directoryBy(roomDir.path),
                        aliasTitle = roomDir.aliasTitle,
                        createdAt = roomDir.createdAt,
                        updatedAt = roomDir.updatedAt,
                    )
                }
            }
            .awaitAll()
    }
}
