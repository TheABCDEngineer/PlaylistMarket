package com.example.playlistmarket.controller.player.interactors

import com.example.playlistmarket.domain.models.TrackPropertiesModel
import com.example.playlistmarket.domain.models.Track

interface TrackPropertiesInteractor {
    fun getTrackProperties(track: Track): TrackPropertiesModel
}