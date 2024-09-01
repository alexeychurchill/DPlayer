package io.alexeychurchill.dplayer.playback.ui

import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.isOutOfBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback

fun Modifier.seekButtonsGesture(
    key1: Any? = null,
    key2: Any? = null,
    onClick: () -> Unit = {},
    onSeekPress: () -> Unit = {},
    onSeekRelease: () -> Unit = {},
): Modifier = composed {
    val haptics = LocalHapticFeedback.current
    pointerInput(key1 = key1, key2 = key2) {
        awaitEachGesture {
            val pointer = awaitFirstDown(requireUnconsumed = true)
            val pointerLongPress = awaitLongPressOrCancellation(pointer.id)
            if (pointerLongPress == null) {
                pointer.consume()
                onClick()
            } else {
                pointerLongPress.consume()
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                onSeekPress()
                awaitReleaseOrDragOut(pointerLongPress.id)
                onSeekRelease()
            }
        }
    }
}

private suspend fun AwaitPointerEventScope.awaitReleaseOrDragOut(pointerId: PointerId) {
    var finished = false
    while (!finished) {
        val pointerRelease = awaitDragOrCancellation(pointerId)
        finished = pointerRelease == null || pointerRelease.isOutOfBounds(
            size,
            extendedTouchPadding,
        )
    }
}
