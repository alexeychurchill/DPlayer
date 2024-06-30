package io.alexeychurchill.clown.core.data.filesystem

import io.alexeychurchill.clown.core.domain.filesystem.FileName
import io.alexeychurchill.clown.core.domain.filesystem.FileSystemEntry
import javax.inject.Inject

class FilesystemStore @Inject constructor(
    @ForTree
    private val treeFactory: DocumentFileFactory,
    @ForFile
    private val fileFactory: DocumentFileFactory,
) {

    fun directoryBy(path: String): FileSystemEntry.Directory? {
        val dirFile = treeFactory(path) ?: return null
        return FileSystemEntry.Directory(
            path = path,
            name = FileName.of(dirFile.name),
            exists = dirFile.exists(),
        )
    }

    fun fileBy(path: String): FileSystemEntry.File? {
        val file = fileFactory(path) ?: return null
        return FileSystemEntry.File(
            path = path,
            name = FileName.of(file.name)
        )
    }

    fun list(path: String): List<FileSystemEntry> {
        val dirFile = treeFactory(path)
            ?.takeIf { it.exists() && it.isDirectory }
            ?: return emptyList()
        return dirFile
            .listFiles()
            .mapNotNull { docFile ->
                when {
                    docFile.isFile -> fileBy(docFile.uri.toString())
                    docFile.isDirectory -> directoryBy(docFile.uri.toString())
                    else -> null
                }
            }
    }
}
