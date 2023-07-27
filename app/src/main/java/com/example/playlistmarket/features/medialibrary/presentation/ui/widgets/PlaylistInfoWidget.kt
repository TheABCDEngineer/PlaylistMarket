package com.example.playlistmarket.features.medialibrary.presentation.ui.widgets

import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.playlistmarket.databinding.FragmentPlaylistPropertiesBinding
import com.example.playlistmarket.features.medialibrary.domain.PlaylistScreenModel
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.presentation.ui.recyclerView.TrackAdapter

class PlaylistInfoWidget(
    private val binding: FragmentPlaylistPropertiesBinding,
    private val feedAdapter: TrackAdapter
) {
   init {
       binding.playlistPropertiesTrackList.adapter = feedAdapter
   }

    fun updatePlaylistProperties(model: PlaylistScreenModel) {
        val scaleType =
            if (model.artwork != null) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        binding.apply {
            playlistPropertiesArtwork.setImageURI(null)
            playlistPropertiesArtwork.setImageURI(model.artwork)
            playlistPropertiesArtwork.scaleType = scaleType
            playlistPropertiesTitle.text = model.title
            playlistPropertiesDescription.text = model.description
            playlistPropertiesTotalDuration.text = model.totalDuration
            playlistPropertiesTracksQuantity.text = model.trackQuantity
        }
    }

    fun updatePlaylistInfoCard(model: PlaylistScreenModel) {
        val scaleType =
            if (model.artwork != null) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        binding.playlistPropertiesMenuPlaylistView.apply {
            recyclerSinglePlaylistArtwork.setImageURI(null)
            recyclerSinglePlaylistArtwork.setImageURI(model.artwork)
            recyclerSinglePlaylistArtwork.scaleType = scaleType
            recyclerSinglePlaylistTitle.text = model.title
            recyclerSinglePlaylistTracksQuantity.text = model.trackQuantity
        }
    }

    fun updateTracksFeed(items: ArrayList<Track>) {
        binding.playlistPropertiesTrackList.isVisible = items.isNotEmpty()
        binding.playlistPropertiesStatusText.isVisible = items.isEmpty()
        feedAdapter.updateItems(items)
        binding.playlistPropertiesTrackList.adapter!!.notifyDataSetChanged()
    }
}