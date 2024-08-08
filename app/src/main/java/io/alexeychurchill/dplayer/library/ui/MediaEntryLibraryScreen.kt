package io.alexeychurchill.dplayer.library.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.library.presentation.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.MediaEntryContentViewModel
import io.alexeychurchill.dplayer.library.presentation.OnLibraryAction

@Composable
fun MediaEntryLibraryScreen(
    payload: String,
    modifier: Modifier = Modifier,
    viewModel: MediaEntryContentViewModel = hiltViewModel(
        creationCallback = { factory: MediaEntryContentViewModel.Factory ->
            factory.create(payload)
        },
    ),
    onLibraryAction: OnLibraryAction = {},
) {
    BackHandler { onLibraryAction(LibraryAction.GoBack) }

    val title by viewModel.titleState.collectAsState()
    val state by viewModel.libraryState.collectAsState()
    LibraryScreenLayout(
        modifier = modifier.fillMaxSize(),
        title = title,
        state = state,
        navigationIcon = {
            IconButton(onClick = { onLibraryAction(LibraryAction.GoBack) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        onLibraryAction = onLibraryAction,
    )
}
