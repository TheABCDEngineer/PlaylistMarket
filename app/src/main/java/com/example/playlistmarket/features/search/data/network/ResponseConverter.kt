package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.features.main.data.dataConverter.TrackConverter
import com.example.playlistmarket.features.main.domain.model.Track
import com.example.playlistmarket.features.search.data.dto.Response
import com.example.playlistmarket.features.search.data.dto.TracksResponse
import com.example.playlistmarket.features.search.domain.enums.QueryError
import com.example.playlistmarket.features.search.domain.model.ResponseModel

object ResponseConverter {
    fun convertToDomain(response: Response): ResponseModel {
        var errorStatus = when (response.responseCode) {
            in 200..299 -> QueryError.NO_ERRORS
            in 400..499 -> QueryError.NO_INTERNET_CONNECTION
            else -> QueryError.SOMETHING_WENT_WRONG
        }

        val resultList = ArrayList<Track>()
        if (errorStatus == QueryError.NO_ERRORS) {
            resultList.addAll(TrackConverter.convertDtoToTrackModel((response as TracksResponse).results))
            if (resultList.isEmpty()) errorStatus = QueryError.NO_RESULTS
        }

        return ResponseModel(
            errorStatus,
            resultList
        )
    }
}