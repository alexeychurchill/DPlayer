@file:OptIn(ExperimentalGlideComposeApi::class)

package io.alexeychurchill.dplayer.library.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Image
import androidx.compose.material.icons.twotone.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.model.FileEntryViewState
import io.alexeychurchill.dplayer.media.presentation.CoverArtPath
import io.alexeychurchill.dplayer.ui.theme.DPlayerTheme

@Composable
fun FileEntryItem(
    state: FileEntryViewState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
) {
    ListItem(
        modifier = modifier
            .clickable(onClick = onTap),
        headlineContent = {
            Text(
                text = state.visibleTitle,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        supportingContent = {
            ArtistName(
                modifier = Modifier.padding(top = 4.dp),
                value = state.artist,
            )
        },
        leadingContent = {
            CoverArt(coverArtPath = state.coverArtPath)
        },
    )
}

@Composable
private fun ArtistName(
    value: String?,
    modifier: Modifier = Modifier,
) {
    val defaultStyle = MaterialTheme.typography.bodyMedium
    Text(
        modifier = modifier,
        text = value ?: stringResource(R.string.library_item_track_artist_unknown),
        overflow = TextOverflow.Clip,
        maxLines = 1,
        style = defaultStyle.copy(
            fontWeight = FontWeight.SemiBold,
            fontStyle = if (value == null) FontStyle.Italic else defaultStyle.fontStyle,
        ),
    )
}

@Composable
private fun CoverArt(
    coverArtPath: CoverArtPath?,
    modifier: Modifier = Modifier,
    extension: String? = null,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .clipToBounds()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
    ) {
        if (coverArtPath == null) {
            CoverArtPlaceholder(
                modifier = Modifier.fillMaxSize(),
                extension = extension,
            )
            return
        }

        GlideSubcomposition(
            modifier = Modifier.fillMaxSize(),
            model = coverArtPath,
        ) {
            when (state) {
                RequestState.Loading, RequestState.Failure -> {
                    CoverArtPlaceholder(
                        modifier = Modifier.fillMaxSize(),
                        extension = extension,
                    )
                }

                is RequestState.Success -> {
                    CoverArtImage(
                        modifier = Modifier.fillMaxSize(),
                        imagePainter = painter,
                        extension = extension,
                    )
                }
            }
        }
    }
}

@Composable
private fun CoverArtPlaceholder(
    modifier: Modifier = Modifier,
    extension: String? = null,
) {
    ConstraintLayout(modifier) {
        val guideline = createGuidelineFromBottom(fraction = 0.45f)
        val (iconRef, extensionRef) = createRefs()

        Icon(
            modifier = Modifier
                .size(24.dp)
                .constrainAs(iconRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(guideline)
                },
            imageVector = Icons.TwoTone.MusicNote,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )

        extension?.let { extension ->
            FileExtensionLabel(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .constrainAs(extensionRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(guideline)
                    },
                extension = extension,
            )
        }
    }
}

@Composable
private fun CoverArtImage(
    imagePainter: Painter,
    modifier: Modifier = Modifier,
    extension: String? = null,
) {
    ConstraintLayout(modifier) {
        val guideline = createGuidelineFromBottom(fraction = 0.45f)
        val (imageRef, extensionRef) = createRefs()

        Image(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(imageRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            painter = imagePainter,
            contentDescription = null,
        )

        extension?.let { extension ->
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .constrainAs(extensionRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(guideline)
                    },
            ) {
                FileExtensionLabel(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    extension = extension,
                )
            }
        }
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

@Preview
@Composable
private fun CoverArtPlaceholderPreview() {
    DPlayerTheme {
        CoverArtPlaceholder(
            modifier = Modifier
                .size(56.dp)
                .background(color = MaterialTheme.colorScheme.primaryContainer),
            extension = "mp3",
        )
    }
}

@Preview
@Composable
private fun CoverArtImagePreview() {
    DPlayerTheme {
        CoverArtImage(
            modifier = Modifier
                .size(56.dp)
                .background(color = Color.White),
            extension = "mp3",
            imagePainter = rememberVectorPainter(image = Icons.TwoTone.Image),
        )
    }
}
