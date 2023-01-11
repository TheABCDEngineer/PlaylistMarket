package com.example.playlistmarket.search.recycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.medialibrary.Track

class SearchTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView = itemView.findViewById(R.id.search_cardview_track_title)
    private val artistNameView: TextView = itemView.findViewById(R.id.search_cardview_track_artist)
    private val trackTimeView: TextView = itemView.findViewById(R.id.search_cardview_track_time)
    private val artworkView: ImageView = itemView.findViewById(R.id.search_cardview_track_picture)

    fun bind(trackData: Track) {
        trackNameView.text = trackData.getTrackName()
        artistNameView.text = trackData.getArtistName()
        trackTimeView.text = trackData.getFormattedTrackTime(true)

        Glide.with(itemView)
            .load(trackData.getArtwork(100))
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(artworkView)
    }
}