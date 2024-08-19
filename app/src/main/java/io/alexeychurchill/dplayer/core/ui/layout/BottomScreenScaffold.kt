@file:OptIn(ExperimentalFoundationApi::class)

package io.alexeychurchill.dplayer.core.ui.layout

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffoldScreen.Bottom
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffoldScreen.Main

enum class BottomScreenScaffoldScreen {
    Main,
    Bottom,
}

@Composable
fun BottomScreenScaffold(
    content: @Composable () -> Unit,
    bottomScreen: @Composable () -> Unit,
    bottomScreenSize: Dp,
    modifier: Modifier = Modifier,
) {

    // TODO: Add bottom navigation bar padding to the bottomScreenSize

    var verticalExtent by remember { mutableStateOf<Float?>(null) }

    val density = LocalDensity.current

    val anchors = DraggableAnchors {
        Main at 0.0f
        verticalExtent?.let {
            Bottom at it
        }
    }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = Main,
            positionalThreshold = { position -> 0.5f * position },
            velocityThreshold = { 1.5f * with(density) { bottomScreenSize.toPx() } },
            animationSpec = tween(),
        )
    }

    LaunchedEffect(key1 = verticalExtent) {
        if (verticalExtent != null) {
            draggableState.updateAnchors(anchors)
        }
    }

    BottomScreenLayout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { (_, height) -> verticalExtent = height.toFloat() },
            ) {
                content()
            }
        },
        bottomScreen = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .anchoredDraggable(
                        state = draggableState,
                        orientation = Orientation.Vertical,
                        reverseDirection = true,
                    ),
            ) {
                bottomScreen()
            }
        },
        bottomScreenSize = bottomScreenSize,
        verticalOffset = -(draggableState.offset.takeUnless { it.isNaN() } ?: 0.0f),
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
