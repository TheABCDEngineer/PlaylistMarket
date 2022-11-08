package com.example.playlistmarket

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SearchTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val trackTimeView: TextView
    private val artworkView: ImageView

    init {
        trackNameView = itemView.findViewById(R.id.search_cardview_track_title)
        artistNameView = itemView.findViewById(R.id.search_cardview_track_artist)
        trackTimeView = itemView.findViewById(R.id.search_cardview_track_time)
        artworkView = itemView.findViewById(R.id.search_cardview_track_picture)
    }

    fun bind(trackData: Track) {
        trackNameView.text = trackData.trackName
        artistNameView.text = trackData.artistName
        trackTimeView.text = trackData.trackTime
        Glide.with(itemView)
            .load(trackData.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(artworkView)
    }
}