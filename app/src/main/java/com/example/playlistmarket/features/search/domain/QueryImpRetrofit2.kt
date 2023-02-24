package com.example.playlistmarket.features.search.domain

import com.example.playlistmarket.features.search.presenter.enums.QueryError
import com.example.playlistmarket.App
import com.example.playlistmarket.data.repository.QueryRepository
import com.example.playlistmarket.creator.observe.Observable
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.data.dto.TracksResponse
import com.example.playlistmarket.domain.dataConverter.TrackConverter
import com.example.playlistmarket.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QueryImpRetrofit2 : Observable, QueryRepository {

    private lateinit var queryAuthor: Observer
    private val resultList = ArrayList<Track>()
    private var errorStatus: QueryError = QueryError.NO_ERRORS

    override fun addObserver(observer: Observer) {
        queryAuthor = observer
    }

    override fun executeQuery(queryValue: String?) {
        resultList.clear()
        if (queryValue == null) {
            errorStatus = QueryError.NO_RESULTS
            executeCallback()
            return
        }

        App.serviceApi!!.findTrack(queryValue).enqueue(object :
            Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                errorStatus = if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        resultList.addAll(
                           TrackConverter.convertDtoToTrackModel(response.body()?.results!!)
                        )
                        QueryError.NO_ERRORS
                    } else {
                        QueryError.NO_RESULTS
                    }
                } else {
                    QueryError.SOMETHING_WENT_WRONG
                }
                executeCallback()
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                errorStatus = QueryError.NO_INTERNET_CONNECTION
                executeCallback()
            }
        })

    }

    override fun executeCallback() {
        queryAuthor.notifyObserver(errorStatus, resultList)
    }
}