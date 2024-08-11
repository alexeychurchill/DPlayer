@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.Label
import androidx.compose.material.icons.twotone.NewLabel
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.viewmodel.SetAliasNameMode
import io.alexeychurchill.dplayer.library.presentation.viewmodel.EditAliasViewModel
import io.alexeychurchill.dplayer.library.presentation.viewmodel.EditAliasViewModel.Factory
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@Composable
fun EditAliasNameBottomSheet(
    directoryUri: String,
    viewModel: EditAliasViewModel = hiltViewModel(
        key = "$directoryUri:${System.currentTimeMillis()}",
        creationCallback = { factory: Factory -> factory.create(directoryUri) },
    ),
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onClose: () -> Unit = {},
) {
    LaunchedEffect(key1 = viewModel, key2 = sheetState) {
        viewModel.onDoneEvent.collectLatest {
            sheetState.hide()
            onClose()
        }
    }

    val mode by viewModel.mode.collectAsState()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp, left = 0.dp, right = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp
                )
                .navigationBarsPadding()
                .imePadding()
                .imeNestedScroll(),
        ) {

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = when (mode) {
                    SetAliasNameMode.Set -> Icons.TwoTone.NewLabel
                    else -> Icons.AutoMirrored.TwoTone.Label
                },
                contentDescription = null,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = when (mode) {
                    SetAliasNameMode.Set -> stringResource(R.string.library_alias_title_set)
                    SetAliasNameMode.Update -> stringResource(R.string.library_alias_title_update)
                    null -> stringResource(R.string.generic_update)
                },
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            AliasValue(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = viewModel.aliasValue,
                onValueChange = viewModel::onAliasValueChange,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.End,
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                val cancelEnabled by viewModel.cancelEnabled.collectAsState()
                OutlinedButton(
                    onClick = viewModel::onCancel,
                    enabled = cancelEnabled
                ) {
                    Text(text = stringResource(R.string.generic_cancel))
                }

                val applyEnabled by viewModel.applyEnabled.collectAsState()
                Button(
                    onClick = viewModel::onApply,
                    enabled = applyEnabled,
                ) {
                    Text(
                        text = when (mode) {
                            SetAliasNameMode.Set -> stringResource(R.string.generic_apply)
                            SetAliasNameMode.Update -> stringResource(R.string.generic_update)
                            null -> stringResource(R.string.generic_loading)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AliasValue(
    value: StateFlow<String?>,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    var aliasText by remember { mutableStateOf(TextFieldValue()) }
    LaunchedEffect(key1 = value) {
        val initialValue = value
            .filterNotNull()
            .first()

        aliasText = aliasText.copy(
            text = initialValue,
            selection = TextRange(initialValue.length),
        )
        focusRequester.requestFocus()
    }

    OutlinedTextField(
        modifier = modifier
            .focusRequester(focusRequester),
        value = aliasText,
        onValueChange = { newAliasText ->
            aliasText = newAliasText
            onValueChange(newAliasText.text)
        },
        maxLines = 1,
        label = {
            Text(text = stringResource(R.string.library_alias_value_label))
        },
        placeholder = {
            Text(text = stringResource(R.string.library_alias_value_placeholder))
        },
    )
}
