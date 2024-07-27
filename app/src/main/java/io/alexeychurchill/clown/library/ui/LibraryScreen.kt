package io.alexeychurchill.clown.library.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.alexeychurchill.clown.R
import io.alexeychurchill.clown.library.presentation.LibraryAction
import io.alexeychurchill.clown.library.presentation.LibraryDirection
import io.alexeychurchill.clown.library.presentation.LibraryItemsViewModel
import io.alexeychurchill.clown.library.presentation.LibraryViewModel
import io.alexeychurchill.clown.library.presentation.MediaEntryItemsViewModel
import io.alexeychurchill.clown.library.presentation.OnLibraryAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LibraryScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    TreePickerEffect(openPickerFlow = viewModel.openDirectoryFlow) { uriPath ->
        viewModel.onAction(LibraryAction.TreePicked(uriPath))
    }

    LaunchedEffect(key1 = viewModel) {
        viewModel.directionFlow.collectLatest { direction ->
            navController.navigate(direction.navPath)
        }
    }

    NavHost(navController, startDestination = LibraryDirection.start) {
        composable(route = LibraryDirection.Root.navPath) {
            RootLibraryScreen(onLibraryAction = viewModel::onAction)
        }

        composable(route = LibraryDirection.Directory.NavPattern) { backStackEntry ->
            val directoryPath = backStackEntry.arguments
                ?.getString(LibraryDirection.Directory.ArgPath)

            MediaEntryLibraryScreen(
                path = directoryPath,
                onLibraryAction = viewModel::onAction,
            )
        }
    }
}

@Composable
private fun RootLibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: LibraryItemsViewModel = hiltViewModel(),
    onLibraryAction: OnLibraryAction = {},
) {
    val state by viewModel.libraryViewState.collectAsState()
    LibraryScreenLayout(
        modifier = modifier.fillMaxSize(),
        title = stringResource(R.string.library_title),
        state = state,
        actions = {
            AddButton {
                onLibraryAction(LibraryAction.OpenTreePicker)
            }
        },
        onLibraryAction = onLibraryAction,
    )
}

@Composable
private fun MediaEntryLibraryScreen(
    path: String?,
    modifier: Modifier = Modifier,
    viewModel: MediaEntryItemsViewModel = hiltViewModel(
        creationCallback = { factory: MediaEntryItemsViewModel.Factory -> factory.create(path) }
    ),
    onLibraryAction: OnLibraryAction = {},
) {
    val state by viewModel.libraryState.collectAsState()
    LibraryScreenLayout(
        modifier = modifier.fillMaxSize(),
        title = "...",
        state = state,
        onLibraryAction = onLibraryAction,
    )
}

@Composable
private fun TreePickerEffect(
    openPickerFlow: Flow<Unit>,
    onPicked: (path: String?) -> Unit,
) {
    val treePicker = rememberLauncherForActivityResult(OpenDocumentTree()) { uri ->
        onPicked(uri?.toString())
    }

    LaunchedEffect(key1 = openPickerFlow, key2 = onPicked) {
        openPickerFlow.collectLatest { treePicker.launch(null) }
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

