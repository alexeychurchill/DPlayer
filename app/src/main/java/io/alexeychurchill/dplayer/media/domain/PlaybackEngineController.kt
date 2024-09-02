package io.alexeychurchill.dplayer.media.domain

import io.alexeychurchill.dplayer.core.domain.MediaId

interface PlaybackEngineController {

    suspend fun use(mediaId: MediaId)

    fun play()

    fun pause()

    fun togglePlayback()

    fun seek(positionMs: Long)
}
