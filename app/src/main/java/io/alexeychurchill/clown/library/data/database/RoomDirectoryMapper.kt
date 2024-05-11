package io.alexeychurchill.clown.library.data.database

import io.alexeychurchill.clown.library.domain.Directory
import javax.inject.Inject

class RoomDirectoryMapper @Inject constructor() {

    fun mapToDomain(roomDirectory: RoomDirectory): Directory {
        return Directory(
            path = roomDirectory.path,
            aliasTitle = roomDirectory.aliasTitle,
            addedAt = roomDirectory.addedAt,
            updatedAt = roomDirectory.updatedAt
        )
    }

    fun mapToRoom(directory: Directory): RoomDirectory {
        return RoomDirectory(
            path = directory.path,
            aliasTitle = directory.aliasTitle,
            addedAt = directory.addedAt,
            updatedAt = directory.updatedAt
        )
    }
}
