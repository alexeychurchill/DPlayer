package io.alexeychurchill.dplayer.library.data

import io.alexeychurchill.dplayer.core.domain.filesystem.FilesExtensions
import io.alexeychurchill.dplayer.library.domain.EntrySource.FileSystem
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import javax.inject.Inject

class FileSystemRepositoryImpl @Inject constructor(
    private val mediaEntryStore: MediaEntryStore,
) : FileSystemRepository {

    override suspend fun getEntryBy(path: String): MediaEntry {
        return mediaEntryStore.directoryMediaEntry(path, source = FileSystem)
    }

    override suspend fun getEntriesFor(path: String): List<MediaEntry> {
        // TODO: Separate file-music file filtering
        // Actually, it's better to remove MediaEntryStore and construct
        // media entries in the Repositories, using FileSystemStore and another
        // resources only.
        //
        // The MediaEntryStore is kind of temporary thing.
        return mediaEntryStore.listMediaEntries(path).filter { mediaEntry ->
            if (mediaEntry !is MediaEntry.File) {
                return@filter true
            }

            val extension = mediaEntry.fileEntry.extension ?: ""
            extension in FilesExtensions.MusicFiles
        }
    }
}
