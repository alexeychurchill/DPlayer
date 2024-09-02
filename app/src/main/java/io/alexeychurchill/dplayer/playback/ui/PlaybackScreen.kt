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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.FastForward
import androidx.compose.material.icons.twotone.FastRewind
import androidx.compose.material.icons.twotone.Pause
import androidx.compose.material.icons.twotone.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.core.ui.widgets.CoverArtPlaceholder
import io.alexeychurchill.dplayer.media.domain.PlaybackStatus
import io.alexeychurchill.dplayer.playback.presentation.CollapsedPlaybackViewModel
import io.alexeychurchill.dplayer.playback.presentation.PlaybackViewModel

private val PlaybackFlowButtonSize = 80.dp
private val PlaybackFlowButtonIconSize = 40.dp

@Composable
fun PlaybackScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaybackViewModel = hiltViewModel(),
    collapsedViewModel: CollapsedPlaybackViewModel = hiltViewModel(),
    progress: Float = 0.0f,
    onOpen: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        val density = LocalDensity.current
        val bottomSystemPadding = with(density) {
            WindowInsets.systemBars.getBottom(density).toDp()
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            val toolbarRef = createRef()
            val trackInfoRef = createRef()
            val sliderRef = createRef()
            val (timeControlsRef, flowControlsRef) = createRefs()

            createVerticalChain(
                toolbarRef,
                sliderRef.withChainParams(topMargin = 48.dp),
                trackInfoRef.withChainParams(topMargin = 16.dp),
                timeControlsRef.withChainParams(topMargin = 32.dp),
                flowControlsRef.withChainParams(
                    topMargin = 32.dp,
                    bottomMargin = bottomSystemPadding,
                ),
                chainStyle = ChainStyle.Packed,
            )

            val collapsedState by collapsedViewModel.state.collectAsState()
            PlaybackToolbar(
                modifier = Modifier
                    .constrainAs(toolbarRef) {
                        width = fillToConstraints
                        top.linkTo(parent.top)
                        bottom.linkTo(sliderRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        verticalBias = 0.0f
                    },
                state = collapsedState,
                progress = 1.0f - progress,
                onOpen = onOpen,
                onClose = onClose,
                onPlayPause = collapsedViewModel::togglePlayPause,
                onNext = collapsedViewModel::nextTrack,
            )

            CoverArtSlider(
                modifier = Modifier
                    .constrainAs(sliderRef) {
                        top.linkTo(toolbarRef.bottom)
                        bottom.linkTo(trackInfoRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )

            val trackTitle by viewModel.trackTitle.collectAsState()
            val artistName by viewModel.artistName.collectAsState()
            TrackInfo(
                modifier = Modifier
                    .constrainAs(trackInfoRef) {
                        width = fillToConstraints
                        top.linkTo(sliderRef.bottom)
                        bottom.linkTo(timeControlsRef.top)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                trackTitle = trackTitle,
                artistName = artistName,
            )

            // Playback time controls
            PlaybackTimeControls(
                modifier = Modifier
                    .constrainAs(timeControlsRef) {
                        width = fillToConstraints
                        top.linkTo(trackInfoRef.bottom)
                        bottom.linkTo(flowControlsRef.top)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                viewModel = viewModel,
            )

            // Playback flow controls
            PlaybackFlowControls(
                modifier = Modifier
                    .constrainAs(flowControlsRef) {
                        top.linkTo(timeControlsRef.bottom)
                        bottom.linkTo(parent.bottom, margin = bottomSystemPadding)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                viewModel = viewModel,
            )
        }
    }
}

@Composable
private fun CoverArtSlider(
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { 7 })
    val borderColor = MaterialTheme.colorScheme.onTertiaryContainer
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
private fun TrackInfo(
    trackTitle: String?,
    artistName: String?,
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
            targetState = trackTitle,
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
            targetState = artistName,
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
    viewModel: PlaybackViewModel,
    modifier: Modifier = Modifier,
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
        val progress by viewModel.trackProgress.collectAsState()
        Slider(
            modifier = Modifier
                .fillMaxWidth(),
            value = progress ?: 0.0f,
            enabled = progress != null,
            onValueChange = { value ->
                isChangeInProgress = true
                viewModel.seek(value)
            },
            onValueChangeFinished = {
                isChangeInProgress = false
                viewModel.stopSeeking()
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

            val elapsedTimeText by viewModel.elapsedTimeString.collectAsState()
            Text(
                text = elapsedTimeText,
                style = timeTextStyle,
            )

            val totalTimeText by viewModel.totalTimeString.collectAsState()
            Text(
                text = totalTimeText,
                style = timeTextStyle,
            )
        }
    }
}

@Composable
private fun PlaybackFlowControls(
    viewModel: PlaybackViewModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(
            modifier = Modifier
                .size(PlaybackFlowButtonSize)
                .seekButtonsGesture(
                    key1 = viewModel,
                    onClick = { /* TODO */ },
                    onSeekPress = { /* TODO */ },
                    onSeekRelease = { /* TODO */ },
                ),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(PlaybackFlowButtonIconSize),
                imageVector = Icons.TwoTone.FastRewind,
                contentDescription = null,
                tint = LocalContentColor.current,
            )
        }

        val playbackStatus by viewModel.playbackState.collectAsState()
        PlaybackButton(
            modifier = Modifier.size(PlaybackFlowButtonSize),
            status = playbackStatus,
            onClick = viewModel::playPause,
        )

        Box(
            modifier = Modifier
                .size(PlaybackFlowButtonSize)
                .seekButtonsGesture(
                    key1 = viewModel,
                    onClick = { /* TODO */ },
                    onSeekPress = { /* TODO */ },
                    onSeekRelease = { /* TODO */ },
                ),
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(PlaybackFlowButtonIconSize),
                imageVector = Icons.TwoTone.FastForward,
                contentDescription = null,
                tint = LocalContentColor.current,
            )
        }
    }
}

@Composable
private fun PlaybackButton(
    status: PlaybackStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    if (status == PlaybackStatus.Loading) {
        Box(
            modifier = modifier,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(PlaybackFlowButtonIconSize),
            )
        }
    } else {
        FilledIconButton(
            modifier = modifier,
            enabled = status != PlaybackStatus.Idle,
            onClick = onClick,
        ) {
            val iconVector = when (status) {
                PlaybackStatus.Playing -> Icons.TwoTone.Pause
                else -> Icons.TwoTone.PlayArrow
            }

            Icon(
                modifier = Modifier
                    .size(PlaybackFlowButtonIconSize),
                imageVector = iconVector,
                contentDescription = null,
            )
        }
    }
}
