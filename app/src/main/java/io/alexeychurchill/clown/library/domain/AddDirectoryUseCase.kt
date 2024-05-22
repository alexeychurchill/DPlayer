package io.alexeychurchill.clown.library.domain

import io.alexeychurchill.clown.core.domain.time.DateTimeProvider
import javax.inject.Inject

class AddDirectoryUseCase @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val directoryRepository: DirectoryRepository,
) {

    suspend operator fun invoke(path: String?) {
        if (path == null) {
            return
        }
        val directory = directoryRepository.getDirectory(path)
        if (directory != null) {
            throw DirectoryAlreadyAddedException()
        }
        val now = dateTimeProvider.current()
        val newDirectory = Directory(
            path = path,
            name = FileName.Unknown,
            aliasTitle = null,
            addedAt = now,
            updatedAt = now,
            status = DirectoryStatus.Unknown,
            fileCount = null,
            dirCount = null,
        )
        directoryRepository.addDirectory(newDirectory)
    }
}

class DirectoryAlreadyAddedException : Exception()
