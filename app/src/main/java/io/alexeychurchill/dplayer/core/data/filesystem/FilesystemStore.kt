package io.alexeychurchill.dplayer.core.data.filesystem

import io.alexeychurchill.dplayer.core.domain.filesystem.FileName
import io.alexeychurchill.dplayer.core.domain.filesystem.FileSystemEntry
import io.alexeychurchill.dplayer.core.domain.filesystem.FilesExtensions.Separator
import javax.inject.Inject

class FilesystemStore @Inject constructor(
    @ForTree
    private val treeFactory: DocumentFileFactory,
    @ForFile
    private val fileFactory: DocumentFileFactory,
) {

    fun directoryBy(path: String): FileSystemEntry.Directory {
        val dirFile = treeFactory(path)
        return FileSystemEntry.Directory(
            path = path,
            name = FileName.of(dirFile?.name),
            exists = dirFile?.exists() ?: false,
        )
    }

    fun fileBy(path: String): FileSystemEntry.File? {
        val file = fileFactory(path) ?: return null
        return FileSystemEntry.File(
            path = path,
            name = FileName.of(file.name?.withNoExtension),
            extension = file.name?.fileExtension,
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

private val String.withNoExtension: String
    get() = takeIf { it.contains(Separator) && it.last() != Separator }
        ?.dropLastWhile { it != Separator }
        ?.dropLast(n = 1)
        ?: this

private val String.fileExtension: String?
    get() = split(Separator)
        .takeIf { it.size > 1 }
        ?.last()
        ?.lowercase()
