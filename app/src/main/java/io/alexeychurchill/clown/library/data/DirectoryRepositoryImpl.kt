package io.alexeychurchill.clown.library.data

import io.alexeychurchill.clown.library.data.database.DirectoryDao
import io.alexeychurchill.clown.library.data.database.RoomDirectoryMapper
import io.alexeychurchill.clown.library.domain.Directory
import io.alexeychurchill.clown.library.domain.DirectoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DirectoryRepositoryImpl @Inject constructor(
    private val directoryDao: DirectoryDao,
    private val mapper: RoomDirectoryMapper
) : DirectoryRepository {

    override val allDirectories: Flow<List<Directory>>
        get() = directoryDao
            .allDirectoriesByAddedDate()
            .map { it.map(mapper::mapToDomain) }

    override suspend fun addDirectory(directory: Directory) {
        directoryDao.insertDirectory(mapper.mapToRoom(directory))
    }
}
