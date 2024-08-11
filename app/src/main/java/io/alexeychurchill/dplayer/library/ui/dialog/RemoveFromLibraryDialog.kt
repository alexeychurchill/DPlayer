package io.alexeychurchill.dplayer.library.ui.dialog

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.RemoveCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.viewmodel.RemoveFromLibraryViewModel
import io.alexeychurchill.dplayer.library.presentation.viewmodel.RemoveFromLibraryViewModel.Factory
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RemoveFromLibraryDialog(
    directoryUri: String,
    directoryTitle: String,
    viewModel: RemoveFromLibraryViewModel = hiltViewModel(
        key = "$directoryUri:${System.currentTimeMillis()}",
        creationCallback = { factory: Factory -> factory.create(directoryUri) },
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
        dismissButton = {
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                onClick = onDismiss,
            ) {
                Text(text = stringResource(R.string.generic_cancel))
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                ),
                onClick = viewModel::confirmRemove,
            ) {
                Text(text = stringResource(R.string.generic_remove))
            }
        },
        iconContentColor = MaterialTheme.colorScheme.error,
        icon = {
            Icon(
                imageVector = Icons.TwoTone.RemoveCircle,
                contentDescription = null,
            )
        },
        title = {
            Text(text = stringResource(R.string.library_root_entry_remove_title))
        },
        text = {
            val text = buildAnnotatedString {
                append(stringResource(R.string.library_root_entry_remove_description_start))
                append(' ')
                withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                    append(directoryTitle)
                }
                append(' ')
                append(stringResource(R.string.library_root_entry_remove_description_end))
            }
            Text(text = text)
        },
    )
}
