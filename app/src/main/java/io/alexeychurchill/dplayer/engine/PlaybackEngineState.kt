package io.alexeychurchill.dplayer.engine

import io.alexeychurchill.dplayer.media.domain.FileMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlaybackEngineState {

    val playbackStatus: StateFlow<PlaybackStatus>

    val fileMetadata: StateFlow<FileMetadata?>

    val trackDuration: StateFlow<Long?>

    val trackElapsed: Flow<Long?>
}
