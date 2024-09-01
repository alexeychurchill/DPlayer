package io.alexeychurchill.dplayer.engine

import io.alexeychurchill.dplayer.core.domain.MediaId

interface PlaybackEngine {

    suspend fun use(mediaId: MediaId)
}
