package io.alexeychurchill.dplayer.playback.presentation

import io.alexeychurchill.dplayer.media.presentation.CoverArtPath

data class PlaybackFlowViewState(
    val controlsEnabled: Boolean,
    val playbackState: PlaybackStatusViewState,
)

enum class PlaybackStatusViewState {
    Unknown,
    Playing,
    Paused,
}

data class PlayingTrackInfoViewState(
    val title: String? = null,
    val artist: String? = null,
)

sealed interface CollapsedPlaybackViewState {

    data object Empty : CollapsedPlaybackViewState

    data class Track(
        val title: String? = null,
        val coverArtPath: CoverArtPath? = null,
        val isPlaybackEnabled: Boolean = false,
        val isNextEnabled: Boolean = false,
    ) : CollapsedPlaybackViewState
}

sealed interface PlaybackAction {

    data object TogglePlayback : PlaybackAction

    data object Rewind : PlaybackAction

    data object Next : PlaybackAction

    data object FastRewindStart : PlaybackAction

    data object FastRewindStop : PlaybackAction

    data object FastForwardStart : PlaybackAction

    data object FastForwardStop : PlaybackAction
}
