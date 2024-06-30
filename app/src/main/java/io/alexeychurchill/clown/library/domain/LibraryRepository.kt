package io.alexeychurchill.clown.library.domain

import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    val allEntries: Flow<List<LibraryEntry>>

    suspend fun getLibraryEntry(path: String): LibraryEntry?

    suspend fun addLibraryRecord(record: LibraryRecord)
}
