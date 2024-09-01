package io.alexeychurchill.dplayer.engine

import io.alexeychurchill.dplayer.core.domain.MediaId

interface PlaybackEngineController {

    suspend fun use(mediaId: MediaId)
}
