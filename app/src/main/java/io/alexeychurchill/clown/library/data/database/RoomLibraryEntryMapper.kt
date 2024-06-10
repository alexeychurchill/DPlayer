package io.alexeychurchill.clown.library.data.database

import io.alexeychurchill.clown.library.domain.LibraryEntry
import javax.inject.Inject

class RoomLibraryEntryMapper @Inject constructor() {

    fun mapToRoom(entry: LibraryEntry): RoomLibraryEntry {
        return RoomLibraryEntry(
            path = entry.directory?.path ?: throw IllegalArgumentException(
                "Cannot insert library entry with no path!"
            ),
            aliasTitle = entry.aliasTitle,
            createdAt = entry.createdAt,
            updatedAt = entry.updatedAt
        )
    }
}
