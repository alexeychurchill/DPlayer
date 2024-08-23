@file:OptIn(ExperimentalFoundationApi::class)

package io.alexeychurchill.dplayer.core.ui.layout

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffoldDefaults.PositionalThresholdFactor
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffoldDefaults.VelocityThresholdFactor
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffoldScreen.Bottom
import io.alexeychurchill.dplayer.core.ui.layout.BottomScreenScaffoldScreen.Main

enum class BottomScreenScaffoldScreen {
    Main,
    Bottom,
}

internal object BottomScreenScaffoldDefaults {

    const val PositionalThresholdFactor = 0.5f

    const val VelocityThresholdFactor = 3.5f
}

class BottomScreenScaffoldState internal constructor(
    internal val anchoredDraggableState: AnchoredDraggableState<BottomScreenScaffoldScreen>,
) {

    private var verticalExtent by mutableStateOf<Float?>(value = null)

    internal val absoluteOffset: Float
        get() = anchoredDraggableState.offset.takeUnless { it.isNaN() } ?: 0.0f

    val relativeOffset: Float
        get() = verticalExtent?.let { extent -> absoluteOffset / extent } ?: 0.0f

    val currentScreen: BottomScreenScaffoldScreen
        get() = anchoredDraggableState.currentValue

    internal constructor(
        initialScreen: BottomScreenScaffoldScreen,
        density: Density,
        bottomScreenPeekHeight: Dp,
        snapAnimationSpec: AnimationSpec<Float> = tween(),
        decayAnimationSpec: DecayAnimationSpec<Float> = splineBasedDecay(density),
    ) : this(
        anchoredDraggableState = AnchoredDraggableState(
            initialValue = initialScreen,
            positionalThreshold = { position -> PositionalThresholdFactor * position },
            velocityThreshold = {
                VelocityThresholdFactor * with(density) { bottomScreenPeekHeight.toPx() }
            },
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = decayAnimationSpec,
        )
    )

    suspend fun openMain() {
        anchoredDraggableState.animateTo(Main)
    }

    suspend fun openBottom() {
        anchoredDraggableState.animateTo(Bottom)
    }

    internal fun updateExtent(vertical: Float) {
        verticalExtent = vertical
        anchoredDraggableState.updateAnchors(anchors())
    }

    private fun anchors() = DraggableAnchors {
        Main at 0.0f
        verticalExtent?.let { extent ->
            Bottom at extent
        }
    }
}


@Composable
fun rememberBottomScreenScaffoldState(
    bottomScreenPeekHeight: Dp,
    initialScreen: BottomScreenScaffoldScreen = Main,
    animationSpec: AnimationSpec<Float> = tween(),
): BottomScreenScaffoldState {
    val density = LocalDensity.current
    return remember(key1 = density) {
        BottomScreenScaffoldState(
            initialScreen = initialScreen,
            density = density,
            bottomScreenPeekHeight = bottomScreenPeekHeight,
            snapAnimationSpec = animationSpec,
        )
    }
}

@Composable
fun BottomScreenScaffold(
    content: @Composable () -> Unit,
    bottomScreen: @Composable () -> Unit,
    bottomScreenPeekHeight: Dp,
    modifier: Modifier = Modifier,
    state: BottomScreenScaffoldState = rememberBottomScreenScaffoldState(
        bottomScreenPeekHeight = bottomScreenPeekHeight,
    ),
) {
    val density = LocalDensity.current
    val systemBarsInsets = WindowInsets.systemBars
    val systemBottomPadding = with(density) { systemBarsInsets.getBottom(density).toDp() }
    BottomScreenLayout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { (_, height) -> state.updateExtent(height.toFloat()) },
            ) {
                content()
            }
        },
        bottomScreen = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .anchoredDraggable(
                        state = state.anchoredDraggableState,
                        orientation = Orientation.Vertical,
                        reverseDirection = true,
                    ),
            ) {
                bottomScreen()
            }
        },
        bottomScreenSize = bottomScreenPeekHeight + systemBottomPadding,
        verticalOffset = -state.absoluteOffset,
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
