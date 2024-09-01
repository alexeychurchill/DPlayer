package io.alexeychurchill.dplayer.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times

@Preview(widthDp = 48, heightDp = 48)
@Preview(widthDp = 64, heightDp = 64)
@Preview(widthDp = 192, heightDp = 192)
@Composable
fun CoverArtPlaceholder(
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
            ),
        contentAlignment = Alignment.Center,
    ) {
        val minDimension = min(minWidth, minHeight)
        Icon(
            modifier = Modifier.size(0.5f * minDimension),
            imageVector = Icons.TwoTone.MusicNote,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}
