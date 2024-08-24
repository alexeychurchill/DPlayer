@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class,
)

package io.alexeychurchill.dplayer.playback.ui

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.twotone.MusicNote
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.playback.presentation.PlaybackAction
import io.alexeychurchill.dplayer.playback.presentation.PlaybackFlowViewState
import io.alexeychurchill.dplayer.playback.presentation.PlaybackState
import io.alexeychurchill.dplayer.playback.presentation.PlayingTrackInfoViewState

private val PlaybackFlowButtonSize = 80.dp
private val PlaybackFlowButtonIconSize = 40.dp

@Composable
fun PlaybackScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
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

            val (coverArtRef, trackInfoRef) = createRefs()
            TrackInfo(
                modifier = Modifier
                    .constrainAs(trackInfoRef) {
                        width = fillToConstraints
                        top.linkTo(coverArtRef.bottom, margin = 32.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                state = PlayingTrackInfoViewState(
                    title = "Track Title",
                    artist = "Artist Name",
                ),
            )

            val coverArtGuideline = createGuidelineFromTop(fraction = 0.30f)
            CoverArtSlider(
                modifier = Modifier
                    .constrainAs(coverArtRef) {
                        top.linkTo(coverArtGuideline)
                        bottom.linkTo(coverArtGuideline)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )

            // Playback time controls
            val timeControlRef = createRef()
            val seekControlsGuideline = createGuidelineFromTop(fraction = 0.65f)
            PlaybackTimeControls(
                modifier = Modifier
                    .constrainAs(timeControlRef) {
                        width = fillToConstraints
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        top.linkTo(seekControlsGuideline)
                    },
                elapsedTimeText = "000:00:00",
                totalTimeText = "000:00:00",
                elapsedPercent = 0.5f,
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
private fun CoverArtSlider(
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { 7 })
    val borderColor = MaterialTheme.colorScheme.onSecondaryContainer
    HorizontalPager(
        modifier = modifier
            .systemGestureExclusion(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 48.dp),
        pageSpacing = 24.dp,
    ) { pageIndex ->
        val shape = RoundedCornerShape(24.dp)

        val isCurrent by remember {
            derivedStateOf {
                with(pagerState) {
                    !isScrollInProgress && currentPage == settledPage && currentPage == pageIndex
                }
            }
        }

        val borderWidth by animateDpAsState(
            label = "cover art active border width",
            targetValue = if (isCurrent) 4.dp else 0.dp,
        )

        val borderAlpha by animateFloatAsState(
            label = "cover art active border opacity",
            targetValue = if (isCurrent) 1.0f else 0.0f,
        )

        GlideSubcomposition(
            modifier = Modifier
                .clip(shape)
                .border(
                    width = borderWidth,
                    color = borderColor.copy(alpha = borderAlpha),
                    shape = shape,
                )
                .aspectRatio(1.0f),
            model = Uri.parse("file:///android_asset/cover$pageIndex.jpg"),
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
                        contentScale = ContentScale.Fit,
                    )
                }
            }
        }
    }
}

@Composable
private fun CoverArtPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Image(
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.Center),
            imageVector = Icons.TwoTone.MusicNote,
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )
    }
}

@Composable
private fun TrackInfo(
    state: PlayingTrackInfoViewState,
    modifier: Modifier = Modifier,
) {
    val unknownText = stringResource(R.string.playback_screen_track_info_unknown)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.Top,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedContent(
            label = "track title",
            targetState = state.title,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { currentTitle ->
            Text(
                text = currentTitle ?: unknownText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontStyle = if (currentTitle == null) FontStyle.Italic else FontStyle.Normal,
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }

        AnimatedContent(
            label = "track artist",
            targetState = state.artist,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { currentArtist ->
            Text(
                text = currentArtist ?: unknownText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = if (currentArtist == null) FontStyle.Italic else FontStyle.Normal,
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        }
    }
}

@Composable
private fun PlaybackTimeControls(
    elapsedTimeText: String,
    totalTimeText: String,
    elapsedPercent: Float,
    modifier: Modifier = Modifier,
    onElapsedChange: (percent: Float) -> Unit = {},
) {
    var isChangeInProgress by remember { mutableStateOf(false) }
    val sliderTimeSpacing by animateDpAsState(
        label = "space between time text and slider",
        targetValue = if (isChangeInProgress) 4.dp else 0.dp,
    )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            space = sliderTimeSpacing,
            alignment = Alignment.Top,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Slider(
            modifier = Modifier
                .fillMaxWidth(),
            value = elapsedPercent,
            onValueChange = { value ->
                isChangeInProgress = true
                onElapsedChange(value)
            },
            onValueChangeFinished = {
                isChangeInProgress = false
            },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val timeTextStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = elapsedTimeText,
                style = timeTextStyle,
            )

            Text(
                text = totalTimeText,
                style = timeTextStyle,
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
