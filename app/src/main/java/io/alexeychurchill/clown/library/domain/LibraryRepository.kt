package io.alexeychurchill.clown.library.domain

import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    val allEntries: Flow<List<MediaEntry>>

    suspend fun getLibraryEntry(path: String): MediaEntry?

    suspend fun addLibraryRecord(record: LibraryRecord)
}
