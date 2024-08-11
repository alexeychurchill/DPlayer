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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import io.alexeychurchill.dplayer.library.presentation.model.AliasOperationViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.model.LibraryEntryViewState
import io.alexeychurchill.dplayer.library.presentation.model.LibraryRootViewState
import io.alexeychurchill.dplayer.library.presentation.model.OnLibraryAction
import io.alexeychurchill.dplayer.library.presentation.model.OnLibraryDirectoryAction
import io.alexeychurchill.dplayer.library.presentation.viewmodel.LibraryRootViewModel
import io.alexeychurchill.dplayer.library.ui.dialog.EditAliasNameBottomSheet
import io.alexeychurchill.dplayer.library.ui.dialog.RemoveAliasNameDialog
import io.alexeychurchill.dplayer.library.ui.list.DirectoryEntryItem
import io.alexeychurchill.dplayer.library.ui.widgets.LibraryDirectoryMenu
import io.alexeychurchill.dplayer.library.ui.widgets.LibraryLoadingPlaceholder
import io.alexeychurchill.dplayer.library.ui.widgets.LibraryTopBar

@Composable
fun LibraryRootScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryRootViewModel = hiltViewModel(),
    onLibraryAction: OnLibraryAction = {},
) {
    val state by viewModel.libraryViewState.collectAsState()
    val setAliasState by viewModel.aliasOperationState.collectAsState()
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
            state = state,
            modifier = Modifier.padding(screenPaddings),
            onLibraryAction = onLibraryAction,
            onDirectoryAction = viewModel::onDirectoryAction,
        )
    }

    (setAliasState as? AliasOperationViewState.Editing)?.let { editing ->
        EditAliasNameBottomSheet(
            directoryUri = editing.directoryUri,
            onClose = viewModel::onEditAliasDismiss,
        )
    }

    (setAliasState as? AliasOperationViewState.Removing)?.let { removing ->
        RemoveAliasNameDialog(
            directoryUri = removing.directoryUri,
            directoryTitle = removing.directoryTitle,
            onDismiss = viewModel::onRemoveAliasDismiss,
        )
    }
}

@Composable
private fun Content(
    state: LibraryRootViewState,
    modifier: Modifier = Modifier,
    onLibraryAction: OnLibraryAction = {},
    onDirectoryAction: OnLibraryDirectoryAction = {},
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
                    entries = currentState.items,
                    modifier = Modifier.fillMaxSize(),
                    onLibraryAction = onLibraryAction,
                    onDirectoryAction = onDirectoryAction,
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
    onDirectoryAction: OnLibraryDirectoryAction = {},
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
                onDirectoryAction = onDirectoryAction,
            )
        }
    }
}

@Composable
private fun LibraryEntry(
    entryState: LibraryEntryViewState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {},
    onDirectoryAction: OnLibraryDirectoryAction = {},
) {
    var actionsShown by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        DirectoryEntryItem(
            entry = entryState.directory,
            onTap = onTap,
            onLongTap = { actionsShown = true },
        )

        entryState.actions?.let { actions ->
            Box(
                modifier = Modifier
                    .padding(end = 64.dp)
                    .align(Alignment.CenterEnd)
                    .width(64.dp),
            ) {
                LibraryDirectoryMenu(
                    shown = actionsShown,
                    state = actions,
                    onAction = onDirectoryAction,
                    onDismiss = { actionsShown = false },
                )
            }
        }
    }
}
