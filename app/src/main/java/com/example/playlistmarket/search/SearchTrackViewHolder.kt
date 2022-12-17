package com.example.playlistmarket.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.Track
import java.text.SimpleDateFormat
import java.util.*

class SearchTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView = itemView.findViewById(R.id.search_cardview_track_title)
    private val artistNameView: TextView = itemView.findViewById(R.id.search_cardview_track_artist)
    private val trackTimeView: TextView = itemView.findViewById(R.id.search_cardview_track_time)
    private val artworkView: ImageView = itemView.findViewById(R.id.search_cardview_track_picture)

    fun bind(trackData: Track) {
        trackNameView.text = trackData.trackName
        artistNameView.text = trackData.artistName
        trackTimeView.text = trackData.formatTrackTimeFromMillis()

        Glide.with(itemView)
            .load(trackData.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(artworkView)
    }
}