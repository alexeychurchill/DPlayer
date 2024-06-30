package io.alexeychurchill.clown.library.data.database

import io.alexeychurchill.clown.library.domain.LibraryRecord
import javax.inject.Inject

class RoomLibraryRecordMapper @Inject constructor() {

    fun mapToRoom(record: LibraryRecord): RoomLibraryRecord {
        return RoomLibraryRecord(
            path = record.dirPath,
            aliasTitle = record.aliasTitle,
            createdAt = record.createdAt,
            updatedAt = record.updatedAt,
        )
    }
}
