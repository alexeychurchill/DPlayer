package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.core.data.filesystem.FilesystemStore
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.clown.core.domain.filesystem.FilesExtensions
import io.alexeychurchill.clown.core.domain.filesystem.directoryCount
import io.alexeychurchill.clown.core.domain.filesystem.fileCount
import io.alexeychurchill.clown.library.domain.DirectorySource
import io.alexeychurchill.clown.library.domain.MediaEntry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MediaEntryStore @Inject constructor(
    private val filesystemStore: FilesystemStore,
) {

    fun directoryMediaEntry(
        path: String,
        source: DirectorySource = DirectorySource.FromFileSystem,
    ): MediaEntry.Directory {
        val childEntries = filesystemStore.list(path)
        return MediaEntry.Directory(
            directoryEntry = filesystemStore.directoryBy(path),
            musicFileCount = childEntries.fileCount { ext -> ext in FilesExtensions.MusicFiles },
            subDirectoryCount = childEntries.directoryCount(),
            source = source,
        )
    }

    suspend fun listMediaEntries(path: String): List<MediaEntry> = coroutineScope {
        val list = filesystemStore.list(path)
        return@coroutineScope list
            .map { child ->
                async {
                    when (child) {
                        is FileSystemEntry.Directory -> directoryMediaEntry(path = child.path)
                        is FileSystemEntry.File -> MediaEntry.File(fileEntry = child)
                    }
                }
            }
            .awaitAll()
    }
}
