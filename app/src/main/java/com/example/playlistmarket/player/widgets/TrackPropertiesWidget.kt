package com.example.playlistmarket.player.widgets

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.playlistmarket.R
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.player.PlayerActivity

class TrackPropertiesWidget(
    private val track: Track,
    private val activity: PlayerActivity,
    artworkId: Int,
    trackNameId: Int,
    artistNameId: Int,
    lengthId: Int,
    albumId: Int,
    releaseId: Int,
    genreId: Int,
    countryId: Int
) {
    private val artwork: ImageView = activity.findViewById(artworkId)
    private val trackName: TextView = activity.findViewById(trackNameId)
    private val artistName: TextView = activity.findViewById(artistNameId)

    private val lenght: TextView = activity.findViewById(lengthId)
    private val album: TextView = activity.findViewById(albumId)
    private val release: TextView = activity.findViewById(releaseId)
    private val genre: TextView = activity.findViewById(genreId)
    private val country: TextView = activity.findViewById(countryId)

    fun showTrackProperties() {
        Glide.with(activity.applicationContext)
            .load(track.getArtwork(512))
            .centerCrop()
            .placeholder(R.drawable.default_album_image)
            .into(artwork)

        trackName.text = track.getTrackName()
        artistName.text = track.getArtistName()
        lenght.text = track.getTrackTime("mm:ss")
        album.text = track.getCollectionName()
        release.text = track.getReleaseYear()
        genre.text = track.getGenre()
        country.text = track.getCountry()
    }
}
