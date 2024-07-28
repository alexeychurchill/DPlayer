package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.library.domain.DirectorySource.FromFileSystem
import io.alexeychurchill.clown.library.domain.FileSystemRepository
import io.alexeychurchill.clown.library.domain.MediaEntry
import javax.inject.Inject

class FileSystemRepositoryImpl @Inject constructor(
    private val mediaEntryStore: MediaEntryStore,
) : FileSystemRepository {

    override suspend fun getEntryBy(path: String): MediaEntry {
        return mediaEntryStore.directoryMediaEntry(path, source = FromFileSystem)
    }

    override suspend fun getEntriesFor(path: String): List<MediaEntry> {
        return mediaEntryStore.listMediaEntries(path)
    }
}
