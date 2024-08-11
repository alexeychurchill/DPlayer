package io.alexeychurchill.dplayer.library.domain

import io.alexeychurchill.dplayer.core.domain.time.DateTimeProvider
import javax.inject.Inject

class AddDirectoryUseCase @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val libraryRepository: LibraryRepository,
) {

    suspend operator fun invoke(path: String?) {
        if (path == null) {
            return
        }
        if (libraryRepository.getLibraryEntry(path) != null) {
            throw DirectoryAlreadyAddedException()
        }
        val now = dateTimeProvider.current()
        val record = LibraryRecord(
            dirPath = path,
            aliasTitle = null,
            createdAt = now,
            updatedAt = now,
        )
        libraryRepository.putLibraryRecord(record)
    }
}

class DirectoryAlreadyAddedException : Exception()
