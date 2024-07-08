package io.alexeychurchill.clown.library.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.alexeychurchill.clown.library.viewstate.LibraryListItemState
import io.alexeychurchill.clown.library.viewstate.LibraryListItemState.Entry.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun EntryLibraryListItem(
    @PreviewParameter(LibraryListItemEntryPreviewProvider::class)
    itemState: LibraryListItemState.Entry,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    ListItem(
        modifier = modifier.clickable {
            scope.launch { itemState.onTap() }
        },
        headlineContent = {
            Text(
                text = itemState.title,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            ProvideLibraryMetaDefaults {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LibraryMetaPanel(items = itemState.metaItems)
                }
            }
        },
        leadingContent = {
            EntryTypeIcon(itemType = itemState.type)
        },
        trailingContent = {
            StatusIcon(status = itemState.status)
        },
    )
}

@Composable
private fun EntryTypeIcon(
    itemType: LibraryListItemState.Entry.Type,
    modifier: Modifier = Modifier,
) {
    val iconVector = when (itemType) {
        LibraryListItemState.Entry.Type.Directory -> Icons.TwoTone.Folder
        LibraryListItemState.Entry.Type.MusicFile -> Icons.TwoTone.MusicNote
        LibraryListItemState.Entry.Type.None -> null
    }
    iconVector?.let {
        Icon(
            modifier = modifier.size(24.dp),
            imageVector = iconVector,
            contentDescription = null,
        )
    }
}

@Composable
private fun StatusIcon(
    status: Status,
    modifier: Modifier = Modifier,
) {
    val iconVector = when (status) {
        Status.Openable -> Icons.TwoTone.ChevronRight
        Status.Faulty -> Icons.TwoTone.Warning
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
