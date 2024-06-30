package io.alexeychurchill.clown.library.domain

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.clown.core.domain.time.DateTimeProvider
import javax.inject.Inject

class AddDirectoryUseCase @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val libraryRepository: LibraryRepository,
) {

    suspend operator fun invoke(path: String?) {
        if (path == null) {
            return
        }
        if (libraryRepository.getDirectory(path) != null) {
            throw DirectoryAlreadyAddedException()
        }
        val now = dateTimeProvider.current()
        val directory = FileSystemEntry.Directory( // TODO: Get from the FS
            path = path,
            name = FileName.Unknown,
            exists = true,
        )
        // TODO: Get rid of LibraryEntry usage
        val entry = LibraryEntry(
            directory = directory,
            aliasTitle = null,
            createdAt = now,
            updatedAt = now,
            directoryCount = 0,
            musicFileCount = 0,
        )
        libraryRepository.addDirectory(entry)
    }
}

class DirectoryAlreadyAddedException : Exception()
