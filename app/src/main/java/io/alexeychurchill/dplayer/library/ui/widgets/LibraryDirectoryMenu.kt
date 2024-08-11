package io.alexeychurchill.dplayer.library.ui.widgets

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryAction.SetAlias
import io.alexeychurchill.dplayer.library.presentation.model.LibraryDirectoryActionsViewState
import io.alexeychurchill.dplayer.library.presentation.model.OnLibraryDirectoryAction

@Composable
fun LibraryDirectoryMenu(
    state: LibraryDirectoryActionsViewState,
    modifier: Modifier = Modifier,
    onAction: OnLibraryDirectoryAction,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = true,
        onDismissRequest = onDismiss,
    ) {
        if (state.setAliasEnabled) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(R.string.library_root_action_set_alias))
                },
                onClick = {
                    onDismiss()
                    onAction(SetAlias)
                }
            )
        }
    }
}
