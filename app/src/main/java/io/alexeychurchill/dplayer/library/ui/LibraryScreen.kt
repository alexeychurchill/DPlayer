package io.alexeychurchill.dplayer.library.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocumentTree
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.alexeychurchill.dplayer.library.presentation.LibraryAction
import io.alexeychurchill.dplayer.library.presentation.LibraryDirection
import io.alexeychurchill.dplayer.library.presentation.LibraryDirection.Directory.Companion.ArgPayload
import io.alexeychurchill.dplayer.library.presentation.LibraryViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

private const val TransitionAlpha = 0.35f

@Composable
fun LibraryScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LibraryViewModel = hiltViewModel(),
) {
    TreePickerEffect(openPickerFlow = viewModel.openDirectoryFlow) { uriPath ->
        viewModel.onAction(LibraryAction.TreePicked(uriPath))
    }

    DirectionsEffect(
        directionFlow = viewModel.directionFlow,
        navController = navController,
    )

    BackDirectionEffect(
        backDirectionFlow = viewModel.backDirectionFlow,
        navController = navController,
    )

    NavHost(navController, startDestination = LibraryDirection.start) {
        composable(
            route = LibraryDirection.Root.navPath,
            popEnterTransition = { transitionEnterFromStart() },
            exitTransition = { transitionExitToStart() },
        ) {
            LibraryRootScreen(onLibraryAction = viewModel::onAction)
        }

        composable(
            route = LibraryDirection.Directory.NavPattern,
            enterTransition = { transitionEnterFromEnd() },
            popEnterTransition = { transitionEnterFromStart() },
            exitTransition = { transitionExitToStart() },
            popExitTransition = { transitionExitToEnd() },
        ) { backStackEntry ->
            val payload = backStackEntry
                .arguments
                ?.getString(ArgPayload)
                ?: throw IllegalArgumentException("$ArgPayload is missing!")

            MediaEntryLibraryScreen(
                payload = payload,
                onLibraryAction = viewModel::onAction,
            )
        }
    }
}

private fun transitionEnterFromStart(): EnterTransition {
    return slideIn(
        animationSpec = tween(),
        initialOffset = { size -> IntOffset(x = -size.width, y = 0) },
    ) + fadeIn(
        animationSpec = tween(),
        initialAlpha = TransitionAlpha,
    )
}

private fun transitionEnterFromEnd(): EnterTransition {
    return slideIn(
        animationSpec = tween(),
        initialOffset = { size -> IntOffset(x = size.width, y = 0) },
    ) + fadeIn(
        animationSpec = tween(),
        initialAlpha = TransitionAlpha,
    )
}

private fun transitionExitToStart(): ExitTransition {
    return slideOut(
        animationSpec = tween(),
        targetOffset = { size -> IntOffset(x = -size.width, y = 0) },
    ) + fadeOut(
        animationSpec = tween(),
        targetAlpha = TransitionAlpha,
    )
}

private fun transitionExitToEnd(): ExitTransition {
    return slideOut(
        animationSpec = tween(),
        targetOffset = { size -> IntOffset(x = size.width, y = 0) },
    ) + fadeOut(
        animationSpec = tween(),
        targetAlpha = TransitionAlpha,
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
private fun DirectionsEffect(
    directionFlow: Flow<LibraryDirection>,
    navController: NavController,
) {
    LaunchedEffect(key1 = directionFlow) {
        directionFlow.collectLatest { direction ->
            navController.navigate(direction.navPath)
        }
    }
}

@Composable
private fun BackDirectionEffect(
    backDirectionFlow: Flow<Unit>,
    navController: NavController,
) {
    LaunchedEffect(key1 = backDirectionFlow) {
        backDirectionFlow.collectLatest {
            navController.popBackStack()
        }
    }
}
