package io.alexeychurchill.dplayer.core.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Placeholder(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val boundLinesColor = MaterialTheme.colorScheme.contentColorFor(containerColor)
    Canvas(
        modifier = modifier
            .fillMaxSize(),
    ) {
        val strokeWidth = 2.dp.toPx()
        drawRect(
            color = containerColor,
            topLeft = Offset.Zero,
            size = size,
            style = Fill,
        )
        drawRect(
            color = boundLinesColor,
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = strokeWidth),
        )
        drawLine(
            color = boundLinesColor,
            start = Offset.Zero,
            end = Offset(x = size.width, y = size.height),
            strokeWidth = strokeWidth,
        )
        drawLine(
            color = boundLinesColor,
            start = Offset(x = size.width, y = 0.0f),
            end = Offset(x = 0.0f, y = size.height),
            strokeWidth = strokeWidth,
        )
    }
}
