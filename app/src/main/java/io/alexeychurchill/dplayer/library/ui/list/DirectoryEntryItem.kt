@file:OptIn(ExperimentalFoundationApi::class)

package io.alexeychurchill.dplayer.library.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ChevronRight
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.alexeychurchill.dplayer.library.presentation.model.DirectoryEntryViewState
import io.alexeychurchill.dplayer.library.presentation.model.DirectoryStatusViewState
import io.alexeychurchill.dplayer.library.presentation.model.DirectoryStatusViewState.Faulty
import io.alexeychurchill.dplayer.library.presentation.model.DirectoryStatusViewState.Openable

private const val NoCountValue = "-"

@Composable
fun DirectoryEntryItem(
    entry: DirectoryEntryViewState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
    onLongTap: (() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier
            .combinedClickable(
                onClick = onTap,
                onLongClick = onLongTap,
            ),
        headlineContent = {
            Text(
                text = entry.visibleTitle,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            ChildrenCount(
                modifier = Modifier.padding(top = 4.dp),
                subDirectoryCount = entry.directoryCount,
                fileCount = entry.fileCount,
            )
        },
        leadingContent = {
            DirectoryCover()
        },
        trailingContent = {
            StatusIcon(status = entry.status)
        },
    )
}

@Composable
private fun ChildrenCount(
    subDirectoryCount: Int?,
    fileCount: Int?,
    modifier: Modifier = Modifier,
) {
    val iconSize = 16.dp
    val textStyle = MaterialTheme.typography.bodySmall
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = Icons.TwoTone.Folder,
            contentDescription = null,
        )

        Text(
            text = subDirectoryCount?.toString() ?: NoCountValue,
            style = textStyle,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = Icons.TwoTone.MusicNote,
            contentDescription = null,
        )

        Text(
            text = fileCount?.toString() ?: NoCountValue,
            style = textStyle,
        )
    }
}

@Composable
private fun DirectoryCover(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clipToBounds()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Icon(
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            imageVector = Icons.TwoTone.Folder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun StatusIcon(
    status: DirectoryStatusViewState,
    modifier: Modifier = Modifier,
) {
    val iconVector = when (status) {
        Openable -> Icons.TwoTone.ChevronRight
        Faulty -> Icons.TwoTone.Warning
        else -> null
    }
    Box(modifier = modifier.size(24.dp)) {
        iconVector?.let {
            Icon(
                modifier = modifier.fillMaxSize(),
                imageVector = iconVector,
                contentDescription = null,
            )
        }
    }
}
