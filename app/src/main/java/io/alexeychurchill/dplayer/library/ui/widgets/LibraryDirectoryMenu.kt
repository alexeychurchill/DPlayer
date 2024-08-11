package io.alexeychurchill.dplayer.library.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Label
import androidx.compose.material.icons.twotone.NewLabel
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction.SetAlias
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction.UpdateAlias
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryActionsViewState
import io.alexeychurchill.dplayer.library.presentation.model.OnLibraryDirectoryAction

@Composable
fun LibraryDirectoryMenu(
    shown: Boolean,
    directoryUri: String,
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
                    onAction(SetAlias(directoryUri))
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
                    onAction(UpdateAlias(directoryUri))
                },
            )
        }
    }
}
