package xyz.aprildown.ringtone.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import xyz.aprildown.ringtone.R
import xyz.aprildown.ringtone.data.MusicModel

internal class PickerCustomFragment : PickerBaseFragment() {

    override fun init() = Unit
    override fun shouldShowContextMenu(): Boolean = false

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<MusicListItem>> {
        return CustomMusicLoader(localContext, viewModel.musicModel)
    }

    override fun onLoadFinished(loader: Loader<List<MusicListItem>>, data: List<MusicListItem>?) {
        if (data != null) {
            if (data.isNotEmpty()) {
                musicAdapter.populateData(data)
            } else {
                Toast.makeText(localContext, R.string.no_music_found, Toast.LENGTH_LONG).show()
                parent.customPicked(null)
            }
        }
    }

    override fun onItemClicked(viewHolder: RecyclerView.ViewHolder, id: Int) {
        when (id) {
            MusicAdapter.CLICK_NORMAL -> onMusicItemClicked(viewHolder)
        }
    }

    private class CustomMusicLoader(
        context: Context,
        private val musicModel: MusicModel
    ) : AsyncTaskLoader<List<MusicListItem>>(context) {

        override fun onStartLoading() {
            super.onStartLoading()
            forceLoad()
        }

        @SuppressLint("MissingPermission")
        override fun loadInBackground(): List<MusicListItem>? {
            val available = musicModel.getAvailableCustomMusics()
            return List(available.size) {
                val item = available[it]
                SoundItem(
                    SoundItem.TYPE_CUSTOM, item.uri, item.title,
                    false, false
                )
            }
        }
    }
}
