package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.library.domain.MediaEntry
import io.alexeychurchill.clown.library.domain.FileSystemRepository
import javax.inject.Inject

class FileSystemRepositoryImpl @Inject constructor(
    private val mediaEntryStore: MediaEntryStore,
) : FileSystemRepository {

    override suspend fun getEntriesFor(path: String): List<MediaEntry> {
        return mediaEntryStore.listMediaEntries(path)
    }
}
