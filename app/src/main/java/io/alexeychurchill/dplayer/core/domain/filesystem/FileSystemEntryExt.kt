package io.alexeychurchill.dplayer.core.domain.filesystem


fun List<FileSystemEntry>.fileCount(
    extensionPredicate: ((extension: String?) -> Boolean)? = null,
): Int {
    return filterIsInstance<FileSystemEntry.File>().count { file ->
        extensionPredicate?.invoke(file.extension) ?: true
    }
}

fun List<FileSystemEntry>.directoryCount(): Int {
    return filterIsInstance<FileSystemEntry.Directory>().size
}
