@file:OptIn(ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.OnLibraryAction
import io.alexeychurchill.dplayer.library.presentation.model.LibraryEntryViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryRootViewState
import io.alexeychurchill.dplayer.library.presentation.model.SetAliasViewState
import io.alexeychurchill.dplayer.library.presentation.viewmodel.LibraryRootViewModel
import io.alexeychurchill.dplayer.library.ui.list.DirectoryEntryItem

@Composable
fun LibraryRootScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryRootViewModel = hiltViewModel(),
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
        Content(
            modifier = Modifier.padding(screenPaddings),
            state = state,
            onLibraryAction = onLibraryAction,
            onSetAliasTap = viewModel::onSetAliasRequest,
        )
    }

    (setAliasState as? SetAliasViewState.Editing)?.let { editing ->
        SetAliasNameDialog(
            directoryUri = editing.directoryUri,
            onClose = viewModel::onSetAliasDismiss,
        )
    }
}

@Composable
private fun Content(
    state: LibraryRootViewState,
    modifier: Modifier = Modifier,
    onLibraryAction: OnLibraryAction = {},
    onSetAliasTap: (String) -> Unit = {},
) {
    Crossfade(
        modifier = modifier,
        targetState = state,
        label = "library root screen state",
    ) { currentState ->
        when (currentState) {
            LibraryRootViewState.Loading -> {
                LibraryLoadingPlaceholder(
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is LibraryRootViewState.Loaded -> {
                LibraryEntriesList(
                    modifier = Modifier.fillMaxSize(),
                    entries = currentState.items,
                    onLibraryAction = onLibraryAction,
                    onSetAliasTap = onSetAliasTap,
                )
            }
        }
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
private fun LibraryEntriesList(
    entries: List<LibraryEntryViewState>,
    modifier: Modifier = Modifier,
    onLibraryAction: OnLibraryAction = {},
    onSetAliasTap: (String) -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        items(
            items = entries,
            key = { it.directory.path },
            contentType = { it::class.simpleName },
        ) { entryState ->
            LibraryEntry(
                entryState = entryState,
                onTap = { onLibraryAction(entryState.directory.openAction) },
                onSetAliasTap = { onSetAliasTap(entryState.directory.path) },
            )
        }
    }
}

@Composable
private fun LibraryEntry(
    entryState: LibraryEntryViewState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
    onSetAliasTap: () -> Unit = {},
) {
    var actionsShown by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        DirectoryEntryItem(
            entry = entryState.directory,
            onTap = onTap,
            onActionsMenuRequest = { actionsShown = true },
        )

        Box(
            modifier = Modifier
                .padding(end = 64.dp)
                .align(Alignment.CenterEnd)
                .width(64.dp),
        ) {
            UserDirectoryMenu___(
                expanded = actionsShown,
                onDismiss = { actionsShown = false },
                onSetAliasTap = onSetAliasTap,
            )
        }
    }
}

@Composable
private fun UserDirectoryMenu___(
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
