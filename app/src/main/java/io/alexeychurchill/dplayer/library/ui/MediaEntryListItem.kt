package io.alexeychurchill.dplayer.library.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState
import io.alexeychurchill.dplayer.library.presentation.SecondaryInfoViewState

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
                maxLines = 1,
                overflow = TextOverflow.Clip,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            entry.secondaryInfo?.let { info ->
                SecondaryInfo(
                    modifier = Modifier.padding(top = 4.dp),
                    state = info,
                )
            }
        },
        leadingContent = {
            MediaEntryCover(
                itemType = entry.type,
                fileExtension = entry.fileExtension,
                coverArtPath = entry.coverArtPath,
            )
        },
        trailingContent = {
            StatusIcon(status = entry.status)
        },
    )
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
private fun SecondaryInfo(
    state: SecondaryInfoViewState,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SecondaryInfoViewState.TrackInfo -> ArtistAndYearInfo(
            modifier = modifier,
            artist = state.artist,
        )

        is SecondaryInfoViewState.DirectoryChildInfo -> DirectoryChildCount(
            modifier = modifier,
            subDirectoryCount = state.subDirectoryCount,
            fileCount = state.fileCount,
        )
    }
}

@Composable
private fun ArtistAndYearInfo(
    modifier: Modifier = Modifier,
    artist: String? = null,
) {
    val isUnknown = artist == null
    val defaultStyle = MaterialTheme.typography.bodyMedium
    Text(
        modifier = modifier,
        text = artist ?: stringResource(R.string.library_item_track_unknown),
        overflow = TextOverflow.Clip,
        maxLines = 1,
        style = defaultStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontStyle = if (isUnknown) FontStyle.Italic else defaultStyle.fontStyle,
        ),
    )
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
