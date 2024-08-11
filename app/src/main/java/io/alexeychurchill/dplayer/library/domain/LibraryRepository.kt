package io.alexeychurchill.dplayer.library.domain

import kotlinx.coroutines.flow.Flow

interface LibraryRepository {

    val allEntries: Flow<List<MediaEntry>>

    suspend fun getLibraryEntry(path: String): MediaEntry?

    suspend fun setDirectoryAlias(uri: String, aliasName: String?)

    suspend fun putLibraryRecord(record: LibraryRecord)

    suspend fun removeEntry(path: String)
}
