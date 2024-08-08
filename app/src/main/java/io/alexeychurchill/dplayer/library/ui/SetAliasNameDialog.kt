@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package io.alexeychurchill.dplayer.library.ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.dplayer.R
import io.alexeychurchill.dplayer.library.presentation.SetAliasViewModel
import io.alexeychurchill.dplayer.library.presentation.SetAliasViewModel.Factory
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SetAliasNameDialog(
    directoryUri: String,
    viewModel: SetAliasViewModel = hiltViewModel(
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

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
        windowInsets = WindowInsets(top = 0.dp, bottom = 0.dp, left = 0.dp, right = 0.dp),
    ) {
        val valueFocusRequester = remember { FocusRequester() }

        LaunchedEffect(key1 = null) {
            valueFocusRequester.requestFocus()
        }

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
                imageVector = Icons.TwoTone.NewLabel,
                contentDescription = null,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = stringResource(R.string.library_alias_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
            )

            val aliasValue by viewModel.aliasValue.collectAsState()
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .focusRequester(valueFocusRequester),
                value = aliasValue,
                onValueChange = viewModel::onAliasValueChange,
                maxLines = 1,
                label = {
                    Text(text = stringResource(R.string.library_alias_value_label))
                },
                placeholder = {
                    Text(text = stringResource(R.string.library_alias_value_placeholder))
                },
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
                    Text(text = "Cancel")
                }

                val applyEnabled by viewModel.applyEnabled.collectAsState()
                Button(
                    onClick = viewModel::onApply,
                    enabled = applyEnabled,
                ) {
                    Text(text = "Apply")
                }
            }
        }
    }
}
