package com.example.playlistmarket.domain.player

import com.example.playlistmarket.controller.player.interactors.TrackPropertiesInteractor
import com.example.playlistmarket.domain.models.TrackPropertiesModel
import com.example.playlistmarket.domain.models.Track

class TrackPropertiesAct : TrackPropertiesInteractor {
    override fun getTrackProperties(track: Track) = TrackPropertiesModel(track)
}
