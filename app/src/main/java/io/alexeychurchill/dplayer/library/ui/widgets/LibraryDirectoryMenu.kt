package io.alexeychurchill.dplayer.library.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Label
import androidx.compose.material.icons.automirrored.twotone.LabelOff
import androidx.compose.material.icons.twotone.NewLabel
import androidx.compose.material.icons.twotone.RemoveCircle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction.RemoveAlias
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction.SetAlias
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction.UpdateAlias
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryActionsViewState
import io.alexeychurchill.dplayer.library.presentation.model.OnLibraryDirectoryAction

@Composable
fun LibraryDirectoryMenu(
    shown: Boolean,
    state: LibraryDirectoryActionsViewState,
    modifier: Modifier = Modifier,
    onAction: OnLibraryDirectoryAction,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = shown,
        onDismissRequest = onDismiss,
    ) {
        if (state.setAliasEnabled) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.TwoTone.NewLabel,
                        contentDescription = null
                    )
                },
                text = {
                    Text(text = stringResource(R.string.library_root_action_set_alias))
                },
                onClick = {
                    onDismiss()
                    onAction(SetAlias(state.directoryUri))
                }
            )
        }

        if (state.updateAliasEnabled) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.Label,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(R.string.library_root_action_update_alias))
                },
                onClick = {
                    onDismiss()
                    onAction(UpdateAlias(state.directoryUri))
                },
            )
        }

        if (state.removeAliasEnabled) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.LabelOff,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(text = stringResource(R.string.library_root_action_remove_alias))
                },
                onClick = {
                    onDismiss()
                    onAction(
                        RemoveAlias(
                            directoryUri = state.directoryUri,
                            directoryTitle = state.directoryTitle,
                        )
                    )
                },
            )
        }

        val errorColor = MaterialTheme.colorScheme.error
        DropdownMenuItem(
            colors = MenuDefaults.itemColors(
                leadingIconColor = errorColor,
                textColor = errorColor,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.TwoTone.RemoveCircle,
                    contentDescription = null,
                )
            },
            text = {
                Text(text = stringResource(R.string.library_root_action_remove_from_library))
            },
            onClick = {
                onDismiss()
                val action = LibraryDirectoryAction.RemoveFromLibrary(
                    directoryUri = state.directoryUri,
                    directoryTitle = state.directoryTitle,
                )
                onAction(action)
            },
        )
    }
}
