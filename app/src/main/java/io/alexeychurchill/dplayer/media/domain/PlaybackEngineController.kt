package io.alexeychurchill.dplayer.media.domain

import io.alexeychurchill.dplayer.core.domain.MediaId

interface PlaybackEngineController {

    suspend fun use(mediaId: MediaId)

    suspend fun play()

    suspend fun pause()

    suspend fun togglePlayback()

    suspend fun seek(positionMs: Long)
}
