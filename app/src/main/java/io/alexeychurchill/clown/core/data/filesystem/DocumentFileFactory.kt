package io.alexeychurchill.clown.core.data.filesystem

import androidx.documentfile.provider.DocumentFile
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ForFile

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ForTree

typealias DocumentFileFactory = (path: String) -> DocumentFile?
