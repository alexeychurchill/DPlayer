@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class,
)

package io.alexeychurchill.dplayer.playback.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FastForward
import androidx.compose.material.icons.twotone.FastRewind
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material.icons.twotone.Pause
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import io.alexeychurchill.dplayer.playback.presentation.PlaybackAction
import io.alexeychurchill.dplayer.playback.presentation.PlaybackFlowViewState
import io.alexeychurchill.dplayer.playback.presentation.PlaybackState

private val PlaybackFlowButtonSize = 80.dp
private val PlaybackFlowButtonIconSize = 40.dp

@Composable
fun PlaybackScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {

        val coverPagerState = rememberPagerState(
            pageCount = { 3 },
        )

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
        ) {

            val closeButtonRef = createRef()
            FilledTonalIconButton(
                modifier = Modifier
                    .constrainAs(closeButtonRef) {
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                onClick = { /*TODO*/ },
            ) {
                Icon(
                    imageVector = Icons.TwoTone.KeyboardArrowDown,
                    contentDescription = null,
                )
            }

            val (coverArtRef, trackNameRef, artistNameRef) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(trackNameRef) {
                        top.linkTo(coverArtRef.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                text = "Track Name",
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                modifier = Modifier
                    .constrainAs(artistNameRef) {
                        top.linkTo(trackNameRef.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                text = "Artist Name",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
            )

            val coverArtGuideline = createGuidelineFromTop(fraction = 0.30f)
            HorizontalPager(
                modifier = Modifier
                    .constrainAs(coverArtRef) {
                        top.linkTo(coverArtGuideline)
                        bottom.linkTo(coverArtGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .systemGestureExclusion(),
                state = coverPagerState,
                contentPadding = PaddingValues(horizontal = 48.dp),
            ) { pageIndex ->
                GlideImage(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .aspectRatio(1.0f),
                    model = Uri.parse("file:///android_asset/cover${pageIndex.inc()}.jpg"),
                    contentDescription = null,
                )
            }

            val (seekbarRef, elapsedRef, remainingRef) = createRefs()
            val seekControlsGuideline = createGuidelineFromTop(fraction = 0.65f)

            Slider(
                modifier = Modifier
                    .constrainAs(seekbarRef) {
                        width = fillToConstraints
                        bottom.linkTo(seekControlsGuideline)
                        start.linkTo(parent.start, margin = 20.dp)
                        end.linkTo(parent.end, margin = 20.dp)
                    },
                value = 0.0f,
                onValueChange = { },
            )

            val timeTextStyle = MaterialTheme.typography.bodyMedium
            Text(
                modifier = Modifier
                    .constrainAs(elapsedRef) {
                        top.linkTo(seekControlsGuideline)
                        start.linkTo(seekbarRef.start, margin = 8.dp)
                    },
                text = "000:00:00",
                style = timeTextStyle,
            )

            Text(
                modifier = Modifier
                    .constrainAs(remainingRef) {
                        top.linkTo(seekControlsGuideline)
                        end.linkTo(seekbarRef.end, margin = 8.dp)
                    },
                text = "000:00:00",
                style = timeTextStyle,
            )

            // Playback flow controls
            val playbackControlsGuideline = createGuidelineFromTop(fraction = 0.80f)
            val playRef = createRef()
            PlaybackFlowControls(
                modifier = Modifier
                    .constrainAs(playRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(playbackControlsGuideline)
                        bottom.linkTo(playbackControlsGuideline)
                    },
                state = PlaybackFlowViewState(
                    controlsEnabled = true,
                    playbackState = PlaybackState.Paused,
                ),
            )
        }
    }
}

@Composable
private fun PlaybackFlowControls(
    state: PlaybackFlowViewState,
    modifier: Modifier = Modifier,
    onAction: (PlaybackAction) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButton(
            modifier = Modifier
                .size(PlaybackFlowButtonSize),
            onClick = { onAction(PlaybackAction.Rewind) },
        ) {
            Icon(
                modifier = Modifier
                    .size(PlaybackFlowButtonIconSize),
                imageVector = Icons.TwoTone.FastRewind,
                contentDescription = null,
            )
        }

        FilledIconButton(
            modifier = Modifier
                .size(PlaybackFlowButtonSize),
            onClick = { onAction(PlaybackAction.TogglePlayback) },
        ) {
            val iconVector = when (state.playbackState) {
                PlaybackState.Playing -> Icons.TwoTone.Pause
                else -> Icons.TwoTone.PlayArrow
            }
            Icon(
                modifier = Modifier
                    .size(PlaybackFlowButtonIconSize),
                imageVector = iconVector,
                contentDescription = null,
            )
        }

        IconButton(
            modifier = Modifier
                .size(PlaybackFlowButtonSize),
            onClick = { onAction(PlaybackAction.Next) },
        ) {
            Icon(
                modifier = Modifier
                    .size(PlaybackFlowButtonIconSize),
                imageVector = Icons.TwoTone.FastForward,
                contentDescription = null,
            )
        }
    }
}
