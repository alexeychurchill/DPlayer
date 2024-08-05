@file:OptIn(ExperimentalGlideComposeApi::class)

package io.alexeychurchill.dplayer.library.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Folder
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material3.Icon
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath

@Composable
fun MediaEntryCover(
    itemType: MediaEntryItemViewState.Type,
    modifier: Modifier = Modifier,
    fileExtension: String? = null,
    coverArtPath: CoverArtPath? = null,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clipToBounds()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
    ) {
        when (itemType) {
            MediaEntryItemViewState.Type.MusicFile -> {
                CoverArtImage(
                    modifier = Modifier.fillMaxSize(),
                    coverArtPath = coverArtPath,
                    overlay = { hasImage ->
                        FileEntryCoverIcon(
                            modifier = Modifier.fillMaxSize(),
                            showIcon = !hasImage,
                            fileExtension = fileExtension,
                        )
                    }
                )
            }

            MediaEntryItemViewState.Type.Directory -> {
                DirectoryEntryCoverIcon(modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                /** NO OP **/
            }
        }
    }
}

@Composable
private fun CoverArtImage(
    coverArtPath: CoverArtPath?,
    modifier: Modifier = Modifier,
    overlay: @Composable (hasImage: Boolean) -> Unit = {},
) {
    if (coverArtPath == null) {
        overlay(false)
        return
    }

    GlideSubcomposition(
        modifier = modifier,
        model = coverArtPath,
    ) {
        when (state) {
            RequestState.Loading, RequestState.Failure -> {
                overlay(false)
            }

            is RequestState.Success -> {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painter,
                    contentDescription = null,
                )

                overlay(true)
            }
        }
    }
}

@Composable
private fun FileEntryCoverIcon(
    modifier: Modifier = Modifier,
    showIcon: Boolean = true,
    fileExtension: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3.0f),
        ) {
            if (showIcon) {
                Icon(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    imageVector = Icons.TwoTone.MusicNote,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.0f),
        ) {
            if (fileExtension != null) {
                FileExtensionBadge(
                    modifier = Modifier.align(Alignment.TopCenter),
                    extension = fileExtension,
                )
            }
        }
    }
}

@Composable
private fun DirectoryEntryCoverIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        modifier = Modifier
            .then(modifier)
            .size(24.dp),
        imageVector = Icons.TwoTone.Folder,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onPrimaryContainer,
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
