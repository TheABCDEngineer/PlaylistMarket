package com.example.playlistmarket.features.player.presentation.ui.recyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel

class PlayerPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val artwork: ImageView = view.findViewById(R.id.recycler_single_playlist_artwork)
    private val title: TextView = view.findViewById(R.id.recycler_single_playlist_title)
    private val tracksQuantity: TextView = view.findViewById(R.id.recycler_single_playlist_tracks_quantity)

    fun bind(model: PlaylistRecyclerModel) {
        if (model.artwork == null) {
            artwork.setImageResource(R.drawable.default_album_image)
            artwork.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            artwork.setImageURI(model.artwork)
            artwork.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        title.text = model.title
        tracksQuantity.text = model.tracksQuantity
    }
}