package io.alexeychurchill.dplayer.media.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PlaybackEngineState {

    val playbackStatus: StateFlow<PlaybackStatus>

    val fileMetadata: StateFlow<FileMetadata?>

    val trackDuration: StateFlow<Long?>

    val trackElapsed: Flow<Long?>
}
