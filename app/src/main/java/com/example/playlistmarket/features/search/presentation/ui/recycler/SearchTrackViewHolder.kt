package com.example.playlistmarket.features.search.presentation.ui.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.features.main.data.dataConverter.TrackConverter
import com.example.playlistmarket.features.main.domain.model.Track

class SearchTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView = itemView.findViewById(R.id.search_cardview_track_title)
    private val artistNameView: TextView = itemView.findViewById(R.id.search_cardview_track_artist)
    private val trackTimeView: TextView = itemView.findViewById(R.id.search_cardview_track_time)
    private val artworkView: ImageView = itemView.findViewById(R.id.search_cardview_track_picture)

    fun bind(trackData: Track) {
        trackNameView.text = trackData.trackName
        artistNameView.text = trackData.artist
        trackTimeView.text = TrackConverter.getFormattedTrackTime(trackData.trackTimeMillis)

        Glide.with(itemView)
            .load(trackData.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(artworkView)
    }
}