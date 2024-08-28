package io.alexeychurchill.dplayer.playback.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffold
import io.alexeychurchill.dplayer.core.ui.layout.rememberBottomScreenScaffoldState
import kotlinx.coroutines.launch

@Composable
fun WithPlaybackScreen(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val bottomScreenScaffoldState = rememberBottomScreenScaffoldState(
        bottomScreenPeekHeight = 96.dp,
    )

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
