package com.example.playlistmarket.search.query

import com.example.playlistmarket.Observable
import com.example.playlistmarket.Observer
import com.example.playlistmarket.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchQuery<T>(
    baseUrl: String,
    api: Class<T>
) : Observable where T : ItunesApi {

    private val apiService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(api)

    private lateinit var queryAuthor: Observer
    private val resultList = ArrayList<Track>()
    private var errorStatus: ResponseHandle? = null

    var isQueryExecuted = false
        private set

    override fun addObserver(observer: Observer) {
        queryAuthor = observer
    }

    fun executeTracksQuery(sample: String?) {
        if (sample == null) return

        apiService.findTrack(sample).enqueue(object :
            Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                errorStatus = if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        resultList.addAll(response.body()?.results!!)
                        null
                    } else {
                        ResponseHandle.NO_RESULTS
                    }
                } else {
                    ResponseHandle.SOMETHING_WENT_WRONG
                }
                isQueryExecuted = true
                executeNotifyObserver()
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                errorStatus = ResponseHandle.NO_INTERNET_CONNECTION
                isQueryExecuted = true
                executeNotifyObserver()
            }
        })

    }

    fun executeNotifyObserver() {
        if (!isQueryExecuted) return
        queryAuthor.notifyObserver(errorStatus, resultList)
    }
}