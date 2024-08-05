package io.alexeychurchill.dplayer.library.data

import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.core.domain.filesystem.FilesExtensions
import io.alexeychurchill.dplayer.library.domain.EntrySource.FileSystem
import io.alexeychurchill.dplayer.library.domain.FileSystemRepository
import io.alexeychurchill.dplayer.library.domain.MediaEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileSystemRepositoryImpl @Inject constructor(
    private val mediaEntryStore: MediaEntryStore,
) : FileSystemRepository {

    override suspend fun getEntryBy(path: String): MediaEntry {
        return mediaEntryStore.directoryMediaEntry(path, source = FileSystem)
    }

    override suspend fun getEntriesFor(path: String): List<MediaEntry> {
        return withContext(Dispatchers.IO) {
            // TODO: Separate file-music file filtering
            // Actually, it's better to remove MediaEntryStore and construct
            // media entries in the Repositories, using FileSystemStore and another
            // resources only.
            //
            // The MediaEntryStore is kind of temporary thing.
            mediaEntryStore.listMediaEntries(path).filter { mediaEntry ->
                if (mediaEntry.fsEntry !is FileSystemEntry.File) {
                    return@filter true
                }

                val extension = mediaEntry.fsEntry.extension ?: ""
                extension in FilesExtensions.MusicFiles
            }
        }
    }
}
