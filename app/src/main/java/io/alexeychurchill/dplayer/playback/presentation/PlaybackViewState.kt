package io.alexeychurchill.dplayer.playback.presentation

import io.alexeychurchill.dplayer.media.presentation.CoverArtPath

data class PlaybackFlowControlsViewState(
    val playbackStatus: PlaybackStatusViewState = PlaybackStatusViewState.Disabled,
    val isRewindEnabled: Boolean = false,
    val isFastForwardEnabled: Boolean = false,
)

enum class PlaybackStatusViewState {
    Disabled,
    Loading,
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

    data object FastRewind : PlaybackAction

    data object FastForward : PlaybackAction

    data object FastRewindStart : PlaybackAction

    data object FastRewindStop : PlaybackAction

    data object FastForwardStart : PlaybackAction

    data object FastForwardStop : PlaybackAction
}
