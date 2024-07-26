package io.alexeychurchill.clown.library.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.alexeychurchill.clown.R
import io.alexeychurchill.clown.library.presentation.RootLibraryViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RootLibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: RootLibraryViewModel = hiltViewModel(),
) {
    val treePicker = rememberLauncherForActivityResult(OpenDocumentTree()) { uri ->
        viewModel.onAddUri(uri?.toString())
    }
    LaunchedEffect(key1 = viewModel) {
        viewModel.openDirectoryFlow.collectLatest {
            treePicker.launch(input = null)
        }
    }

    val state by viewModel.libraryViewState.collectAsState()
    LibraryScreenLayout(
        title = stringResource(R.string.library_title),
        state = state,
        modifier = modifier.fillMaxSize(),
        actions = { AddButton(onTap = viewModel::onAddTap) },
    )
}

@Composable
private fun AddButton(
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
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
