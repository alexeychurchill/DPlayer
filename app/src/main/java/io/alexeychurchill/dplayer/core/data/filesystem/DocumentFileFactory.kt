package io.alexeychurchill.dplayer.core.data.filesystem

import androidx.documentfile.provider.DocumentFile
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ForFile

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ForTree

fun interface DocumentFileFactory {
    operator fun invoke(path: String): DocumentFile?
}
