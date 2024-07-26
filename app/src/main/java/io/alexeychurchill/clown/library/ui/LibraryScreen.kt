package io.alexeychurchill.clown.library.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun LibraryScreen(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController, startDestination = "library") {
        composable(route = "library") {
            RootLibraryScreen()
        }

        composable(route = "library?path={path}") {
            Text(text = "Library child")
        }
    }
}
