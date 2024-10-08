package io.alexeychurchill.dplayer.library.data

import io.alexeychurchill.dplayer.core.data.filesystem.FilesystemStore
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.core.domain.filesystem.FilesExtensions
import io.alexeychurchill.dplayer.core.domain.filesystem.directoryCount
import io.alexeychurchill.dplayer.core.domain.filesystem.fileCount
import io.alexeychurchill.dplayer.library.domain.EntryInfo
import io.alexeychurchill.dplayer.library.domain.EntrySource
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class MediaEntryStore @Inject constructor(
    private val filesystemStore: FilesystemStore,
) {

    fun directoryMediaEntry(
        path: String,
        source: EntrySource = EntrySource.FileSystem,
    ): MediaEntry {
        val childEntries = filesystemStore.list(path)
        return MediaEntry(
            fsEntry = filesystemStore.directoryBy(path),
            source = source,
            info = EntryInfo.Directory(
                directoryCount = childEntries.directoryCount(),
                musicFileCount = childEntries.fileCount { ext ->
                    (ext ?: "") in FilesExtensions.MusicFiles
                },
            ),
        )
    }

    suspend fun listMediaEntries(path: String): List<MediaEntry> = coroutineScope {
        val list = filesystemStore.list(path)
        return@coroutineScope list
            .map { child ->
                async {
                    when (child) {
                        is FileSystemEntry.Directory -> directoryMediaEntry(path = child.path)
                        is FileSystemEntry.File -> MediaEntry(fsEntry = child)
                    }
                }
            }
            .awaitAll()
    }
}
