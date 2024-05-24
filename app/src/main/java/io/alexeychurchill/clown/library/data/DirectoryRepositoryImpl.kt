@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.clown.library.data

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import dagger.hilt.android.qualifiers.ApplicationContext
import io.alexeychurchill.clown.library.data.database.DirectoryDao
import io.alexeychurchill.clown.library.data.database.RoomDirectory
import io.alexeychurchill.clown.library.data.database.RoomDirectoryMapper
import io.alexeychurchill.clown.library.domain.Directory
import io.alexeychurchill.clown.library.domain.DirectoryPermissionsDispatcher
import io.alexeychurchill.clown.library.domain.DirectoryRepository
import io.alexeychurchill.clown.library.domain.DirectoryStatus
import io.alexeychurchill.clown.library.domain.FileName
import io.alexeychurchill.clown.library.domain.FilesExtensions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class DirectoryRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val directoryDao: DirectoryDao,
    private val mapper: RoomDirectoryMapper,
    private val permissionsDispatcher: DirectoryPermissionsDispatcher,
) : DirectoryRepository {

    override val allDirectories: Flow<List<Directory>>
        get() = directoryDao
            .allDirectoriesByAddedDate()
            .mapLatest { roomDirs -> createDirectories(roomDirs) }

    override suspend fun getDirectory(path: String): Directory? {
        return directoryDao.getDirectoryByPath(path)?.let(::createDirectory)
    }

    override suspend fun addDirectory(directory: Directory) {
        permissionsDispatcher.takePermissions(directory.path)
        directoryDao.insertDirectory(mapper.mapToRoom(directory))
    }

    private suspend fun createDirectories(
        roomDirs: List<RoomDirectory>
    ): List<Directory> = coroutineScope {
        roomDirs
            .map { roomDir -> async { createDirectory(roomDir) } }
            .awaitAll()
    }

    private fun createDirectory(roomDirectory: RoomDirectory): Directory {
        val pathUri = Uri.parse(roomDirectory.path)
        val dirFile = DocumentFile.fromTreeUri(context, pathUri)
        val dirCount = dirFile?.dirCount() ?: 0
        val musicFileCount = dirFile?.fileCount {
            FilesExtensions.MusicFiles.contains(it.fileExtension())
        } ?: 0
        val status = when {
            dirFile == null || !dirFile.exists() -> DirectoryStatus.Unavailable
            dirCount > 0 || musicFileCount > 0 -> DirectoryStatus.Available
            dirFile.listFiles().isEmpty() -> DirectoryStatus.Empty
            else -> DirectoryStatus.Unknown
        }
        return Directory(
            path = roomDirectory.path,
            name = FileName.of(dirFile?.name),
            status = status,
            aliasTitle = roomDirectory.aliasTitle,
            addedAt = roomDirectory.addedAt,
            updatedAt = roomDirectory.updatedAt,
            fileCount = musicFileCount,
            dirCount = dirCount,
        )
    }

    private fun DocumentFile.fileCount(
        predicate: (file: DocumentFile) -> Boolean = { true }
    ): Int {
        return takeIf { it.exists() && it.isDirectory }
            ?.listFiles()
            ?.filter(DocumentFile::isFile)
            ?.count(predicate)
            ?: 0
    }

    private fun DocumentFile.dirCount(
        predicate: (file: DocumentFile) -> Boolean = { true }
    ): Int {
        return takeIf { it.exists() && it.isDirectory }
            ?.listFiles()
            ?.filter(DocumentFile::isDirectory)
            ?.count(predicate) ?: 0
    }

    private fun DocumentFile.fileExtension(): String = name
        ?.split(FilesExtensions.Separator)
        ?.takeIf { it.size > 1 }
        ?.last()
        ?.lowercase()
        ?: ""
}
