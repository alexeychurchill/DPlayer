package io.alexeychurchill.dplayer.playback.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffold
import io.alexeychurchill.dplayer.core.ui.layout.rememberBottomScreenScaffoldState
import kotlinx.coroutines.launch

@Composable
fun WithPlaybackScreen(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shouldOpenPlayback: Boolean = false,
) {
    val scope = rememberCoroutineScope()
    val bottomScreenScaffoldState = rememberBottomScreenScaffoldState(
        bottomScreenPeekHeight = 96.dp,
    )

    var openPlayback by rememberSaveable { mutableStateOf(shouldOpenPlayback) }
    LaunchedEffect(key1 = null) {
        if (openPlayback) {
            openPlayback = false
            bottomScreenScaffoldState.openBottom()
        }
    }

    BottomScreenScaffold(
        modifier = modifier,
        state = bottomScreenScaffoldState,
        content = content,
        bottomScreen = {
            PlaybackScreen(
                progress = bottomScreenScaffoldState.relativeOffset,
                onOpen = {
                    scope.launch {
                        bottomScreenScaffoldState.openBottom()
                    }
                },
                onClose = {
                    scope.launch {
                        bottomScreenScaffoldState.openMain()
                    }
                },
            )
        },
    )
}
