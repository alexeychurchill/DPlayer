package io.alexeychurchill.clown.core.domain.filesystem

sealed class FileSystemEntry(open val path: String) {

    data class File(
        override val path: String,
        val name: FileName,
    ) : FileSystemEntry(path)

    data class Directory(
        override val path: String,
        val name: FileName,
        val exists: Boolean,
        val fileCount: Int?,
        val dirCount: Int?,
    ) : FileSystemEntry(path)
}
