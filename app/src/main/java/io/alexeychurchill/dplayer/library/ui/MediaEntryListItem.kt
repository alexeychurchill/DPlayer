package io.alexeychurchill.dplayer.library.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState

private const val NoCountValue = "-"

@Composable
fun MediaEntryListItem(
    entry: MediaEntryItemViewState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = { },
) {
    ListItem(
        modifier = modifier.clickable(onClick = onTap),
        headlineContent = {
            Text(
                text = entry.title,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            entry.directoryChildInfo?.let { info ->
                DirectoryChildCount(
                    modifier = Modifier.padding(top = 4.dp),
                    subDirectoryCount = info.subDirectoryCount,
                    fileCount = info.fileCount,
                )
            }
        },
        leadingContent = {
            EntryCover(
                itemType = entry.type,
                fileExtension = entry.fileExtension,
            )
        },
        trailingContent = {
            StatusIcon(status = entry.status)
        },
    )
}

@Composable
private fun EntryCover(
    itemType: MediaEntryItemViewState.Type,
    modifier: Modifier = Modifier,
    fileExtension: String? = null,
) {
    val iconVector = when (itemType) {
        MediaEntryItemViewState.Type.Directory -> Icons.TwoTone.Folder
        MediaEntryItemViewState.Type.MusicFile -> Icons.TwoTone.MusicNote
        MediaEntryItemViewState.Type.None -> null
    }

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clipToBounds()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (iconVector != null) {
                Icon(
                    modifier = modifier
                        .size(24.dp),
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            if (fileExtension != null) {
                FileExtensionLabel(extension = fileExtension)
            }
        }
    }
}

@Composable
private fun StatusIcon(
    status: MediaEntryItemViewState.Status,
    modifier: Modifier = Modifier,
) {
    val iconVector = when (status) {
        MediaEntryItemViewState.Status.Openable -> Icons.TwoTone.ChevronRight
        MediaEntryItemViewState.Status.Faulty -> Icons.TwoTone.Warning
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

@Composable
private fun DirectoryChildCount(
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
private fun FileExtensionLabel(
    extension: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = extension.uppercase(),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
    )
}

@Composable
private fun FileExtensionBadge(
    extension: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(4.dp),
            ),
    ) {
        FileExtensionLabel(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
            extension = extension,
        )
    }
}
