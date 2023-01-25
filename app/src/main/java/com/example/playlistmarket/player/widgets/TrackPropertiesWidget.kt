package com.example.playlistmarket.player.widgets

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.player.PlayerActivity

class TrackPropertiesWidget(
    track: Track,
    artwork: ImageView,
    trackName: TextView,
    artistName: TextView,
    lenght: TextView,
    album: TextView,
    release: TextView,
    genre: TextView,
    country: TextView,
) {
    init {
        Glide.with(artwork)
            .load(track.getArtwork(512))
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(artwork)

        trackName.text = track.getTrackName()
        artistName.text = track.getArtistName()
        lenght.text = track.getFormattedTrackTime(true)
        album.text = track.getCollectionName()
        release.text = track.getReleaseYear()
        genre.text = track.getGenre()
        country.text = track.getCountry()
    }
}
