package io.alexeychurchill.clown.core.utils

import io.alexeychurchill.clown.core.data.filesystem.FilesExtensions

val String.fileExtension: String?
    get() = split(FilesExtensions.Separator)
        .takeIf { it.size > 1 }
        ?.last()
        ?.lowercase()
