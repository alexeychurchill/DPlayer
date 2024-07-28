package io.alexeychurchill.dplayer.library.domain

interface FileSystemRepository {

    suspend fun getEntryBy(path: String): MediaEntry

    suspend fun getEntriesFor(path: String): List<MediaEntry>
}
