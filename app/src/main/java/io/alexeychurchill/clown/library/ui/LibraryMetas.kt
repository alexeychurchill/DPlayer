package io.alexeychurchill.clown.library.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.alexeychurchill.clown.library.viewstate.LibraryListItemMeta

data class LibraryMetaSizes(
    val iconSize: Dp = 16.dp,
)

val LocalLibraryMetaSizes = compositionLocalOf { LibraryMetaSizes() }

object LibraryMetaStyle {

    val iconSize: Dp
        @Composable
        @ReadOnlyComposable
        get() = LocalLibraryMetaSizes.current.iconSize

    val textStyle: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography.bodySmall
}

@Composable
fun ProvideLibraryMetaDefaults(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalLibraryMetaSizes provides LibraryMetaSizes()) {
        content()
    }
}

@Composable
fun LibraryMetaPanel(
    items: List<LibraryListItemMeta>,
) {
    for (item in items) {
        LibraryMeta(meta = item)
    }
}

@Composable
fun LibraryMeta(
    meta: LibraryListItemMeta,
    modifier: Modifier = Modifier,
) {
    when (meta) {
        is LibraryListItemMeta.DirectoryCount -> {
            DirectoryCountLibraryMeta(
                modifier = modifier,
                count = meta.count,
            )
        }

        is LibraryListItemMeta.MusicFileCount -> {
            MusicFileCountLibraryMeta(
                modifier = modifier,
                count = meta.count,
            )
        }
    }
}

@Composable
fun DirectoryCountLibraryMeta(
    count: Int,
    modifier: Modifier = Modifier,
) {
    SimpleLibraryMeta(
        modifier = modifier,
        vector = Icons.TwoTone.Folder,
        value = "$count",
    )
}

@Composable
fun MusicFileCountLibraryMeta(
    count: Int,
    modifier: Modifier = Modifier,
) {
    SimpleLibraryMeta(
        modifier = modifier,
        vector = Icons.TwoTone.MusicNote,
        value = "$count",
    )
}

@Composable
fun SimpleLibraryMeta(
    vector: ImageVector,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(LibraryMetaStyle.iconSize),
            imageVector = vector,
            contentDescription = null,
        )

        Text(
            text = value,
            style = LibraryMetaStyle.textStyle,
        )
    }
}
