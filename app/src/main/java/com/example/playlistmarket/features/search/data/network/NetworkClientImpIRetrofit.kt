package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.data.dto.Response
import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.dto.TracksResponse
import com.example.playlistmarket.features.search.domain.model.ResponseContainer
import com.example.playlistmarket.features.search.domain.repository.NetworkClientRepository
import retrofit2.Call
import retrofit2.Callback

class NetworkClientImpIRetrofit : NetworkClientRepository {
    override lateinit var callback: (ResponseContainer) -> Unit

    override fun executeRequest(queryValue: String) {
        val request = TracksRequest(queryValue)
        App.serviceApi!!.findTrack(request.expression).enqueue(
            object :
                Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: retrofit2.Response<TracksResponse>
                ) {
                    val body = response.body() ?: Response()

                    callback.invoke(
                        ResponseConverter.convertResponseToSearchModel(
                            body.apply {
                                responseCode = response.code()
                            }
                        )
                    )
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    callback.invoke(
                        ResponseConverter.convertResponseToSearchModel(
                            TracksResponse(ArrayList()).apply { responseCode = 400 }
                        )
                    )
                }
            }
        )
    }
}