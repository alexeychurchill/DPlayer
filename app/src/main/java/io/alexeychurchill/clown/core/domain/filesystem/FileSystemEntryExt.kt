package io.alexeychurchill.clown.core.domain.filesystem


val String.fileExtension: String?
    get() = split(FilesExtensions.Separator)
        .takeIf { it.size > 1 }
        ?.last()
        ?.lowercase()

fun List<FileSystemEntry>.fileCount(
    extensionPredicate: ((extension: String) -> Boolean)? = null,
): Int {
    return filterIsInstance<FileSystemEntry.File>().count { file ->
        val fileExt = (file.name as? FileName.Name)?.value?.fileExtension ?: ""
        extensionPredicate?.invoke(fileExt) ?: true
    }
}

fun List<FileSystemEntry>.directoryCount(): Int {
    return filterIsInstance<FileSystemEntry.Directory>().size
}
