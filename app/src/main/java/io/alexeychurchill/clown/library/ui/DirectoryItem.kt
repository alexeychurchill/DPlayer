package io.alexeychurchill.clown.library.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.AudioFile
import androidx.compose.material.icons.twotone.ChevronRight
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.alexeychurchill.clown.library.viewstate.DirectoryStatusViewState
import io.alexeychurchill.clown.ui.theme.ClownTheme

@Composable
fun DirectoryListItem(
    title: String,
    modifier: Modifier = Modifier,
    fileCount: Int? = null,
    directoryCount: Int? = null,
    status: DirectoryStatusViewState = DirectoryStatusViewState.NONE,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        modifier = modifier.clickable(
            enabled = onClick != null,
            onClick = onClick ?: { }
        ),
        leadingContent = {
            Icon(
                imageVector = Icons.TwoTone.Folder,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            DirectoryContentsInfo(
                modifier = Modifier.padding(start = 8.dp),
                fileCount = fileCount,
                directoryCount = directoryCount
            )
        },
        trailingContent = {
            StatusIcon(status = status)
        }
    )
}

@Composable
private fun DirectoryContentsInfo(
    modifier: Modifier = Modifier,
    fileCount: Int? = null,
    directoryCount: Int? = null
) {
    if (fileCount == null && directoryCount == null) {
        return
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (fileCount != null) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.TwoTone.MusicNote,
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = fileCount.toString(),
            )
        }

        if (fileCount != null && directoryCount != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (directoryCount != null) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.TwoTone.Folder,
                contentDescription = null,
            )

            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = directoryCount.toString(),
            )
        }
    }
}

@Preview(widthDp = 360, showBackground = true)
@Composable
fun LoadingDirectoryListItem(modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }, headlineContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .height(
                        with(LocalDensity.current) {
                            LocalTextStyle.current.fontSize.toDp()
                        }
                    )
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        },
        supportingContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        with(LocalDensity.current) {
                            LocalTextStyle.current.fontSize.toDp()
                        }
                    )
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        },
        trailingContent = {
            Box(modifier = Modifier.size(24.dp))
        }
    )
}

@Composable
private fun StatusIcon(status: DirectoryStatusViewState) {
    val iconVector = when (status) {
        DirectoryStatusViewState.WARNING -> {
            Icons.TwoTone.Warning
        }
        DirectoryStatusViewState.AVAILABLE -> {
            Icons.TwoTone.ChevronRight
        }
        else -> null
    }
    if (iconVector != null) {
        Icon(
            imageVector = iconVector,
            contentDescription = null
        )
    }
}

@SuppressLint("SdCardPath")
@Preview(widthDp = 360, showBackground = true)
@Composable
private fun DirectoryListItemPreview() {
    ClownTheme {
        DirectoryListItem(
            title = "Sample Dir",
            modifier = Modifier.fillMaxWidth(),
            status = DirectoryStatusViewState.AVAILABLE
        )
    }
}
