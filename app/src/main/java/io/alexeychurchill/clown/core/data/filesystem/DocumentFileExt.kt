package io.alexeychurchill.clown.core.data.filesystem

import androidx.documentfile.provider.DocumentFile

val DocumentFile.fileExtension: String?
    get() = name
        ?.split(FilesExtensions.Separator)
        ?.takeIf { it.size > 1 }
        ?.last()
        ?.lowercase()

val DocumentFile.directoryCount: Int
    get() = takeIf { it.exists() && it.isDirectory }
        ?.listFiles()
        ?.filter(DocumentFile::isDirectory)
        ?.size ?: 0

val DocumentFile.musicFileCount: Int
    get() = fileCount { file ->
        file.fileExtension?.let(FilesExtensions.MusicFiles::contains) ?: false
    }

fun DocumentFile.fileCount(predicate: (file: DocumentFile) -> Boolean = { true }): Int {
    return takeIf { it.exists() && it.isDirectory }
        ?.listFiles()
        ?.filter(DocumentFile::isFile)
        ?.count(predicate)
        ?: 0
}
