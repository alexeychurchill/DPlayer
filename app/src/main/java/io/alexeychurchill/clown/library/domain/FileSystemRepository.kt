package io.alexeychurchill.clown.library.domain

interface FileSystemRepository {

    suspend fun getEntriesFor(path: String): List<MediaEntry>
}
