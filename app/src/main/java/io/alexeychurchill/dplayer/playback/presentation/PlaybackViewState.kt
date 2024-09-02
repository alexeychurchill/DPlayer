package io.alexeychurchill.dplayer.playback.presentation

import io.alexeychurchill.dplayer.media.presentation.CoverArtPath

sealed interface CollapsedPlaybackViewState {

    data object Empty : CollapsedPlaybackViewState

    data class Track(
        val title: String? = null,
        val coverArtPath: CoverArtPath? = null,
        val isPlaybackEnabled: Boolean = false,
        val isPlaying: Boolean = false,
        val isNextEnabled: Boolean = false,
    ) : CollapsedPlaybackViewState
}
