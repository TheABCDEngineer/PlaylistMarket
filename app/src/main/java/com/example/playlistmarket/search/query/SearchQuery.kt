package com.example.playlistmarket.search.query

import com.example.playlistmarket.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchQuery : QueryStatusObservable {
    private val context = App.appContext
    private val searchBaseUrl = context.getString(R.string.search_tracks_base_url)

    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private lateinit var queryAuthor: QueryStatusObserver
    private val resultList = ArrayList<Track>()
    var errorStatus: ResponseHandle? = null

    override fun addObserver(observer: QueryStatusObserver) {
        queryAuthor = observer
    }

    fun executeTracksQuery(sample: String?) {
        if (sample == null) return

        itunesService.findTrack(sample).enqueue(object :
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
                queryAuthor.showQueryResults(resultList, errorStatus)
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                errorStatus = ResponseHandle.NO_INTERNET_CONNECTION
                queryAuthor.showQueryResults(resultList, errorStatus)
            }
        })
    }
}