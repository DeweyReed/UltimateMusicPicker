@file:Suppress("unused", "NOTHING_TO_INLINE")

package xyz.aprildown.ringtone

import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Build
import android.support.annotation.AnyRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import xyz.aprildown.ringtone.data.CustomMusic
import java.text.Collator

internal val MUSIC_SILENT: Uri = Uri.EMPTY
internal val NO_MUSIC_URI: Uri = Uri.EMPTY

internal fun MutableList<CustomMusic>.sortWithCollator() {
    val collator = Collator.getInstance()
    sortWith(Comparator { o1, o2 ->
        collator.compare(o1.title, o2.title)
    })
}

internal fun Context.safeContext(): Context =
    takeIf { Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !isDeviceProtectedStorage }?.let {
        ContextCompat.createDeviceProtectedStorageContext(it) ?: it
    } ?: this

/**
 * @param resourceId identifies an application resource
 * @return the Uri by which the application resource is accessed
 */
internal fun Context.getResourceUri(@AnyRes resourceId: Int): Uri = Uri.Builder()
    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
    .authority(packageName)
    .path(resourceId.toString())
    .build()

internal fun isLOrLater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

internal fun isNOrLater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

internal fun isOOrLater(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

//
// View Helpers
//

internal fun ImageView.startDrawableAnimation() {
    (drawable as? Animatable)?.start()
}

internal fun ImageView.stopDrawableAnimation() {
    (drawable as? Animatable)?.run { if (isRunning) stop() }
}


inline fun View.show() {
    visibility = View.VISIBLE
}

inline fun View.hide() {
    visibility = View.INVISIBLE
}

inline fun View.gone() {
    visibility = View.GONE
}