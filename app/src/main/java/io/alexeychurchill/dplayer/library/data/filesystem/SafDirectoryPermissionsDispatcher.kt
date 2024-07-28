package io.alexeychurchill.dplayer.library.data.filesystem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.alexeychurchill.dplayer.library.domain.DirectoryPermissionsDispatcher
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SafDirectoryPermissionsDispatcher @Inject constructor(
) : DirectoryPermissionsDispatcher, DefaultLifecycleObserver {

    private var activityRef: WeakReference<Activity>? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        (owner as? Activity)?.let {
            activityRef = WeakReference(it)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        activityRef = null
        super.onDestroy(owner)
    }

    override suspend fun takePermissions(uri: String) {
        activityRef?.get()?.contentResolver?.takePersistableUriPermission(
            Uri.parse(uri),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    override suspend fun releasePermissions(uri: String) {
        activityRef?.get()?.contentResolver?.releasePersistableUriPermission(
            Uri.parse(uri),
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}
