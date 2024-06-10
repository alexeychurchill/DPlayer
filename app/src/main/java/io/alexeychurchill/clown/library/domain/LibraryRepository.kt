package io.alexeychurchill.clown.library.domain

import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    val allDirectories: Flow<List<LibraryEntry>>

    suspend fun getDirectory(path: String): LibraryEntry?

    suspend fun addDirectory(entry: LibraryEntry)
}
