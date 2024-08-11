@file:OptIn(ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.LabelOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.viewmodel.RemoveAliasViewModel
import io.alexeychurchill.dplayer.library.presentation.viewmodel.RemoveAliasViewModel.Factory
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RemoveAliasNameDialog(
    directoryUri: String,
    directoryTitle: String,
    viewModel: RemoveAliasViewModel = hiltViewModel(
        key = "${directoryUri}:${System.currentTimeMillis()}",
        creationCallback = { factory: Factory ->
            factory.create(directoryUri)
        },
    ),
    onDismiss: () -> Unit = {},
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.onDoneEvent.collectLatest {
            onDismiss()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.TwoTone.LabelOff,
                contentDescription = null,
            )
        },
        title = {
            Text(text = stringResource(R.string.library_alias_title_remove))
        },
        text = {
            Text(
                text = stringResource(R.string.library_alias_description_remove, directoryTitle),
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.confirmRemove()
                    onDismiss()
                },
            ) {
                Text(text = stringResource(R.string.generic_remove))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.generic_cancel))
            }
        },
    )
}
