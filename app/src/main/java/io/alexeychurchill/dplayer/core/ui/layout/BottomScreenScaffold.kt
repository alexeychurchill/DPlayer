package io.alexeychurchill.dplayer.core.ui.layout

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.launch

@Composable
fun BottomScreenScaffold(
    content: @Composable () -> Unit,
    bottomScreen: @Composable () -> Unit,
    bottomScreenSize: Dp,
    modifier: Modifier = Modifier,
) {

    // TODO: Add bottom navigation bar padding to the bottomScreenSize

    val scope = rememberCoroutineScope()

    var isDragInProgress by remember { mutableStateOf(false) }
    var verticalExtent by remember { mutableStateOf<Float?>(null) }
    val verticalOffset = remember { Animatable(initialValue = 0.0f) }

    val draggableState = rememberDraggableState(onDelta = { delta ->
        scope.launch {
            verticalOffset.snapTo(verticalOffset.value + delta)
        }
    })

    LaunchedEffect(key1 = isDragInProgress) {
        val extent = verticalExtent ?: return@LaunchedEffect
        if (!isDragInProgress) {
            val progress = verticalOffset.value / extent
            verticalOffset.animateTo(targetValue = if (progress < 0.35f) 0.0f else extent)
        }
    }

    BottomScreenLayout(
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { (_, height) -> verticalExtent = -height.toFloat() },
            ) {
                content()
            }
        },
        bottomScreen = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .draggable(
                        orientation = Orientation.Vertical,
                        state = draggableState,
                        onDragStarted = {
                            verticalOffset.stop()
                            isDragInProgress = true
                        },
                        onDragStopped = {
                            isDragInProgress = false
                        },
                    ),
            ) {
                bottomScreen()
            }
        },
        bottomScreenSize = bottomScreenSize,
        modifier = modifier,
        verticalOffset = verticalOffset.value,
    )
}

@Composable
private fun BottomScreenLayout(
    content: @Composable () -> Unit,
    bottomScreen: @Composable () -> Unit,
    bottomScreenSize: Dp,
    modifier: Modifier = Modifier,
    verticalOffset: Float = 0.0f,
) {
    Layout(
        modifier = modifier,
        contents = listOf(content, bottomScreen),
    ) { (content, bottomScreen), constraints ->
        val contentConstraints = if (constraints.hasBoundedHeight) {
            constraints.copy(
                maxHeight = constraints.maxHeight - bottomScreenSize.toPx().toInt(),
            )
        } else {
            constraints
        }

        val contentPlacements = content.map { it.measure(contentConstraints) }
        val topOffset = verticalOffset.toInt()
        val baseBottomScreenTopOffset = contentPlacements.maxByOrNull { it.height }?.height ?: 0
        val bottomScreenPlacements = bottomScreen.map { it.measure(constraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlacements.forEach { contentPlacement ->
                contentPlacement.placeRelative(x = 0, y = topOffset)
            }

            bottomScreenPlacements.forEach { bottomScreenPlacement ->
                bottomScreenPlacement.placeRelative(
                    x = 0,
                    y = baseBottomScreenTopOffset + topOffset,
                )
            }
        }
    }
}
