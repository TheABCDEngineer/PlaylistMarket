package com.example.playlistmarket.features.player.presentation.ui.widgets

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.App
import com.example.playlistmarket.features.main.domain.dataConverter.TrackConverter
import com.example.playlistmarket.features.main.domain.model.Track
import com.example.playlistmarket.features.player.presentation.ui.PlayerActivity

class TrackPropertiesWidget(
    playerActivity: PlayerActivity
) {
    private val artwork: ImageView = playerActivity.findViewById(R.id.player_artwork)
    private val trackName: TextView = playerActivity.findViewById(R.id.player_track_name)
    private val artistName: TextView = playerActivity.findViewById(R.id.player_artist_name)
    private val length: TextView = playerActivity.findViewById(R.id.player_track_value_length)
    private val album: TextView = playerActivity.findViewById(R.id.player_track_value_album)
    private val release: TextView = playerActivity.findViewById(R.id.player_track_value_release)
    private val genre: TextView = playerActivity.findViewById(R.id.player_track_value_genre)
    private val country: TextView = playerActivity.findViewById(R.id.player_track_value_country)

    fun showTrackProperties(track: Track) {
        Glide.with(artwork)
            .load(TrackConverter.getArtwork(track.artworkUrl100, 512))
            .centerCrop()
            .placeholder(App.appContext.getDrawable(R.drawable.default_album_image))
            .into(artwork)

        trackName.text = track.trackName
        artistName.text = track.artist
        length.text = TrackConverter.getFormattedTrackTime(track.trackTimeMillis)
        album.text = track.collection
        release.text = track.releaseYear
        genre.text = track.genre
        country.text = track.country
    }
}