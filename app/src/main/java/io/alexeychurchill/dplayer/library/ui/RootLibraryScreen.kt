@file:OptIn(ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.LibraryContentViewModel
import io.alexeychurchill.dplayer.library.presentation.LibrarySectionViewState
import io.alexeychurchill.dplayer.library.presentation.MediaEntryItemViewState
import io.alexeychurchill.dplayer.library.presentation.OnLibraryAction
import io.alexeychurchill.dplayer.library.presentation.SetAliasState

@Composable
fun RootLibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryContentViewModel = hiltViewModel(),
    onLibraryAction: OnLibraryAction = {},
) {
    val state by viewModel.libraryViewState.collectAsState()
    val setAliasState by viewModel.setAliasState.collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            LibraryTopBar(
                title = stringResource(R.string.library_title),
                actions = {
                    AddButton { onLibraryAction(LibraryAction.OpenTreePicker) }
                },
            )
        },
    ) { screenPaddings ->
        LibraryContentLayout(
            state = state,
            screenPaddings = screenPaddings,
        ) { loadedState, paddings ->
            LazyColumn(modifier = Modifier.padding(paddings)) {
                for (section in loadedState.sections) {
                    when (section) {
                        is LibrarySectionViewState.MediaEntries -> {
                            items(
                                items = section.items,
                                contentType = { MediaEntryItemViewState.Type.Directory },
                                key = { it.path },
                            ) { entry ->
                                UserDirectoryEntryListItem(
                                    entry = entry,
                                    onTap = { onLibraryAction(entry.openAction) },
                                    onSetAliasTap = { viewModel.onSetAliasRequest(entry.path) },
                                )
                            }
                        }

                        else -> {
                            /** NO OP **/
                        }
                    }
                }
            }
        }
    }

    (setAliasState as? SetAliasState.Editing)?.let { editing ->
        SetAliasNameDialog(
            directoryUri = editing.directoryUri,
            onClose = viewModel::onSetAliasDismiss,
        )
    }
}

@Composable
private fun AddButton(
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
) {
    IconButton(
        modifier = modifier,
        onClick = onTap,
    ) {
        Icon(
            imageVector = Icons.TwoTone.Add,
            contentDescription = null
        )
    }
}

@Composable
private fun UserDirectoryEntryListItem(
    entry: MediaEntryItemViewState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
    onSetAliasTap: () -> Unit = {},
) {
    var menuShown by remember { mutableStateOf(false) }
    MediaEntryListItem(
        modifier = modifier,
        entry = entry,
        onTap = onTap,
        onSecondaryAction = { menuShown = true },
        secondaryActionMenu = {
            UserDirectoryMenu(
                expanded = menuShown,
                onDismiss = { menuShown = false },
                onSetAliasTap = onSetAliasTap,
            )
        },
    )
}

@Composable
private fun UserDirectoryMenu(
    expanded: Boolean = false,
    onSetAliasTap: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        DropdownMenuItem(
            text = {
                Text(text = stringResource(R.string.library_root_action_set_alias))
            },
            onClick = {
                onDismiss()
                onSetAliasTap()
            },
        )
    }
}
