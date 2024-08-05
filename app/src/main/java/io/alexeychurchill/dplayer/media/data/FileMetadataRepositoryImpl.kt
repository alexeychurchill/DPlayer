@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.media.data

import io.alexeychurchill.dplayer.media.domain.FileMetadata
import io.alexeychurchill.dplayer.media.domain.FileMetadataRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FileMetadataRepositoryImpl @Inject constructor(
    private val metadataStore: MediaMetadataStore,
    private val mapper: MediaMetadataMapper,
) : FileMetadataRepository {

    override suspend fun getMetadata(uri: String): FileMetadata = withContext(IO) {
        val mediaMetadata = metadataStore.retrieve(uri)
        mapper.mapToFileMetadata(mediaMetadata)
    }

    override suspend fun getBatchMetadata(uris: List<String>): Map<String, FileMetadata> {
        return withContext(IO.limitedParallelism(parallelism = 4)) {
            uris
                .map { uri ->
                    async {
                        val mediaMetadata = metadataStore.retrieve(uri)
                        uri to mapper.mapToFileMetadata(mediaMetadata)
                    }
                }
                .awaitAll()
                .toMap()
        }
    }
}
