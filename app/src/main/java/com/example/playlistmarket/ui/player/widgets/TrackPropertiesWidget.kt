package com.example.playlistmarket.ui.player.widgets

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.domain.models.TrackPropertiesModel
import com.example.playlistmarket.ui.player.PlayerActivity

class TrackPropertiesWidget(
    private val playerActivity: PlayerActivity
) {
    private val artwork: ImageView by lazy { playerActivity.findViewById(R.id.player_artwork) }
    private val trackName: TextView by lazy { playerActivity.findViewById(R.id.player_track_name) }
    private val artistName: TextView by lazy { playerActivity.findViewById(R.id.player_artist_name) }
    private val length: TextView by lazy { playerActivity.findViewById(R.id.player_track_value_length) }
    private val album: TextView by lazy { playerActivity.findViewById(R.id.player_track_value_album) }
    private val release: TextView by lazy { playerActivity.findViewById(R.id.player_track_value_release) }
    private val genre: TextView by lazy { playerActivity.findViewById(R.id.player_track_value_genre) }
    private val country: TextView by lazy { playerActivity.findViewById(R.id.player_track_value_country) }

    fun showTrackProperties(model: TrackPropertiesModel) {
        Glide.with(artwork)
            .load(model.artwork)
            .centerCrop()
            .placeholder(model.artworkDefault)
            .into(artwork)

        trackName.text = model.trackName
        artistName.text = model.artistName
        length.text = model.lenght
        album.text = model.album
        release.text = model.release
        genre.text = model.genre
        country.text = model.country
    }
}