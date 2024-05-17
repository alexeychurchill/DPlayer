package io.alexeychurchill.clown.library.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ChevronRight
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    path: String? = null,
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
            if (path != null) {
                Text(
                    text = path,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        trailingContent = {
            StatusIcon(status = status)
        }
    )
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
            modifier = Modifier.fillMaxWidth(),
            title = "Sample Dir",
            path = "/sdcard/mnt/test",
            status = DirectoryStatusViewState.AVAILABLE
        )
    }
}
