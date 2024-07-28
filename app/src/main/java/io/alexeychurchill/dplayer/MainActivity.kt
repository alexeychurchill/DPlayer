package io.alexeychurchill.dplayer

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.AndroidEntryPoint
import io.alexeychurchill.dplayer.library.data.filesystem.SafDirectoryPermissionsDispatcher
import io.alexeychurchill.dplayer.library.ui.LibraryScreen
import io.alexeychurchill.dplayer.ui.theme.DPlayerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var safDirectoryPermissionsDispatcher: SafDirectoryPermissionsDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
        lifecycle.addObserver(safDirectoryPermissionsDispatcher)
        setContent {
            DPlayerTheme {
                LibraryScreen()
            }
        }
    }

    override fun onDestroy() {
        lifecycle.removeObserver(safDirectoryPermissionsDispatcher)
        super.onDestroy()
    }

    private fun setupEdgeToEdge() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        // Workaround, which disables enforced NavBar contrast
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }
}
