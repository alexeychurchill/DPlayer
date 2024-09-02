@file:OptIn(ExperimentalCoroutinesApi::class)

package io.alexeychurchill.dplayer.playback.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alexeychurchill.dplayer.media.domain.PlaybackEngine
import io.alexeychurchill.dplayer.media.domain.PlaybackStatus
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val NoTitleValue = "-"

@HiltViewModel
class CollapsedPlaybackViewModel @Inject constructor(
    private val playbackEngine: PlaybackEngine,
) : ViewModel() {

    private val title = playbackEngine.fileMetadata.map { fileMetadata ->
        fileMetadata?.let { it.title ?: NoTitleValue }
    }

    val state: StateFlow<CollapsedPlaybackViewState> = playbackEngine
        .playbackStatus
        .flatMapLatest { playbackStatus ->
            if (playbackStatus == PlaybackStatus.Idle) {
                createNoTrackFlow()
            } else {
                createTrackFlow(playbackStatus)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CollapsedPlaybackViewState.Empty,
        )

    private fun createNoTrackFlow(): Flow<CollapsedPlaybackViewState> {
        return flowOf(CollapsedPlaybackViewState.Empty)
    }

    private fun createTrackFlow(status: PlaybackStatus): Flow<CollapsedPlaybackViewState> {
        val isPlaybackEnabled = status == PlaybackStatus.Paused || status == PlaybackStatus.Playing
        val isPlaying = status == PlaybackStatus.Playing
        return title.combine(playbackEngine.trackUri) { title, trackUri ->
            CollapsedPlaybackViewState.Track(
                title = title,
                coverArtPath = trackUri?.let(CoverArtPath::LocalUri),
                isPlaybackEnabled = isPlaybackEnabled,
                isPlaying = isPlaying,
            )
        }
    }

    fun togglePlayPause() {
        playbackEngine.togglePlayback()
    }

    fun nextTrack() {
        /* TODO */
    }
}
