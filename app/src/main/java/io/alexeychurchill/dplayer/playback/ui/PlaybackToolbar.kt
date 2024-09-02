@file:OptIn(ExperimentalMotionApi::class)

package io.alexeychurchill.dplayer.playback.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FastForward
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material.icons.twotone.Pause
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.constraintlayout.compose.Dimension.Companion.value
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.core.ui.widgets.CoverArtPlaceholder
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import io.alexeychurchill.dplayer.playback.presentation.CollapsedPlaybackViewState

private const val OpenAreaProgressThreshold = 0.75f

private object Id {
    const val CloseButton = "CloseButton"
    const val PlayPauseButton = "PlayPauseButton"
    const val NextButton = "NextButton"
    const val CoverArt = "CoverArt"
    const val TrackTitle = "TrackTitle"
    const val NoTrackLabel = "NoTrackLabel"
}

@Composable
fun PlaybackToolbar(
    state: CollapsedPlaybackViewState,
    progress: Float,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit = {},
    onClose: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onNext: () -> Unit = {},
) {
    val density = LocalDensity.current
    val systemTopPadding = with(density) { WindowInsets.systemBars.getTop(density).toDp() }
    val barHeight = 96.dp + (1.0f - progress) * systemTopPadding

    Box(modifier = modifier.height(barHeight)) {
        if (progress >= OpenAreaProgressThreshold) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClick = onOpen),
            )
        }

        when (state) {
            CollapsedPlaybackViewState.Empty -> {
                NoTrackLayout(
                    modifier = Modifier.fillMaxSize(),
                    progress = progress,
                    systemTopPadding = systemTopPadding,
                    onClose = onClose,
                )
            }

            is CollapsedPlaybackViewState.Track -> {
                CollapsedPlayback(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    progress = progress,
                    systemTopPadding = systemTopPadding,
                    onClose = onClose,
                    onPlayPause = onPlayPause,
                    onNext = onNext,
                )
            }
        }
    }
}

@Composable
private fun NoTrackLayout(
    progress: Float,
    modifier: Modifier = Modifier,
    systemTopPadding: Dp = 0.dp,
    onClose: () -> Unit = {},
) {
    val scene = remember(key1 = systemTopPadding) {
        createNoTrackMotionScene(systemTopPadding)
    }

    MotionLayout(
        modifier = modifier,
        motionScene = scene,
        progress = progress,
    ) {
        FilledTonalIconButton(
            modifier = Modifier.layoutId(Id.CloseButton),
            onClick = onClose,
        ) {
            Icon(
                imageVector = Icons.TwoTone.KeyboardArrowDown,
                contentDescription = null,
            )
        }

        Text(
            modifier = Modifier.layoutId(Id.NoTrackLabel),
            text = stringResource(R.string.playback_screen_no_track),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun CollapsedPlayback(
    progress: Float,
    state: CollapsedPlaybackViewState.Track,
    modifier: Modifier = Modifier,
    systemTopPadding: Dp = 0.dp,
    onClose: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onNext: () -> Unit = {},
) {
    val scene = remember(key1 = systemTopPadding) {
        createPlaybackMotionScene(systemTopPadding)
    }

    MotionLayout(
        modifier = modifier,
        motionScene = scene,
        progress = progress,
    ) {

        FilledTonalIconButton(
            modifier = Modifier.layoutId(Id.CloseButton),
            onClick = onClose,
        ) {
            Icon(
                imageVector = Icons.TwoTone.KeyboardArrowDown,
                contentDescription = null,
            )
        }

        CoverArt(
            modifier = Modifier
                .layoutId(Id.CoverArt)
                .clip(RoundedCornerShape(8.dp)),
            coverArtPath = state.coverArtPath,
        )

        TrackTitle(
            modifier = Modifier.layoutId(Id.TrackTitle),
            title = state.title ?: stringResource(R.string.playback_screen_track_info_unknown),
            fontStyle = FontStyle.Italic.takeUnless { state.title != null },
        )

        FilledTonalIconButton(
            modifier = Modifier.layoutId(Id.PlayPauseButton),
            enabled = state.isPlaybackEnabled,
            onClick = onPlayPause,
        ) {
            val iconVector = if (state.isPlaying) {
                Icons.TwoTone.Pause
            } else {
                Icons.TwoTone.PlayArrow
            }
            Icon(
                imageVector = iconVector,
                contentDescription = null,
            )
        }

        IconButton(
            modifier = Modifier.layoutId(Id.NextButton),
            onClick = onNext,
        ) {
            Icon(
                imageVector = Icons.TwoTone.FastForward,
                contentDescription = null,
            )
        }
    }
}

private fun createNoTrackMotionScene(systemTopPadding: Dp): MotionScene = MotionScene {
    val closeButtonRef = createRefFor(Id.CloseButton)
    val noTrackLabel = createRefFor(Id.NoTrackLabel)
    defaultTransition(
        from = constraintSet {
            constrain(closeButtonRef) {
                top.linkTo(parent.top, margin = systemTopPadding)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 16.dp)
            }

            constrain(noTrackLabel) {
                bottom.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        },
        to = constraintSet {
            constrain(closeButtonRef) {
                bottom.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }

            constrain(noTrackLabel) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        },
    )
}

private fun createPlaybackMotionScene(systemTopPadding: Dp): MotionScene = MotionScene {
    val closeButtonRef = createRefFor(Id.CloseButton)
    val (playButtonRef, nextButtonRef) = createRefsFor(Id.PlayPauseButton, Id.NextButton)
    val titleRef = createRefFor(Id.TrackTitle)
    val coverArtRef = createRefFor(Id.CoverArt)
    defaultTransition(
        from = constraintSet {
            constrain(closeButtonRef) {
                top.linkTo(parent.top, margin = systemTopPadding)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 16.dp)
            }

            constrain(nextButtonRef) {
                bottom.linkTo(parent.top, margin = 16.dp)
                end.linkTo(closeButtonRef.start, margin = 16.dp)
            }

            constrain(playButtonRef) {
                bottom.linkTo(parent.top, margin = 16.dp)
                end.linkTo(nextButtonRef.start, margin = 16.dp)
            }

            constrain(coverArtRef) {
                width = value(48.dp)
                height = value(48.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.start, margin = 16.dp)
            }

            constrain(titleRef) {
                bottom.linkTo(parent.top, margin = 16.dp)
                start.linkTo(coverArtRef.end, margin = 16.dp)
            }
        },
        to = constraintSet {
            constrain(closeButtonRef) {
                bottom.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }

            constrain(nextButtonRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 16.dp)
            }

            constrain(playButtonRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(nextButtonRef.start, margin = 16.dp)
            }

            constrain(coverArtRef) {
                width = value(48.dp)
                height = value(48.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start, margin = 16.dp)
            }

            constrain(titleRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(coverArtRef.end, margin = 16.dp)
            }
        },
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CoverArt(
    coverArtPath: CoverArtPath?,
    modifier: Modifier = Modifier,
) {
    if (coverArtPath == null) {
        CoverArtPlaceholder(modifier = modifier)
        return
    }

    GlideSubcomposition(
        modifier = modifier,
        model = coverArtPath,
    ) {
        when (state) {
            RequestState.Loading, RequestState.Failure -> {
                CoverArtPlaceholder(modifier = Modifier.fillMaxSize())
            }

            is RequestState.Success -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun TrackTitle(
    title: String,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle? = null,
) {
    AnimatedContent(
        modifier = modifier,
        label = "track title change",
        targetState = title,
        transitionSpec = {
            // @formatter:off
            slideInVertically { fullHeight -> fullHeight } + fadeIn() togetherWith
            slideOutVertically { fullHeight -> -fullHeight } + fadeOut()
            // @formatter:on
        },
    ) { currentTitle ->
        Text(
            text = currentTitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontStyle = fontStyle ?: FontStyle.Normal,
            ),
        )
    }
}
