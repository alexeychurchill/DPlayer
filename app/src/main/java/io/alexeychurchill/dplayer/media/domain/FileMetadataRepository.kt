package io.alexeychurchill.dplayer.media.domain

interface FileMetadataRepository {

    suspend fun getMetadata(uri: String): FileMetadata

    suspend fun getBatchMetadata(uris: List<String>): Map<String, FileMetadata>
}
