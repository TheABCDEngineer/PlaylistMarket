package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.App
import com.example.playlistmarket.features.search.data.dto.Response
import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.dto.TracksResponse
import com.example.playlistmarket.features.search.domain.repository.NetworkClientRepository
import retrofit2.Call
import retrofit2.Callback

class NetworkClientImpRetrofit : NetworkClientRepository {
    override lateinit var callback: (Response) -> Unit

    override fun executeRequest(request: TracksRequest) {
        App.serviceApi!!.findTrack(request.expression).enqueue(object :
            Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: retrofit2.Response<TracksResponse>
            ) {
                val body = response.body() ?: Response()
                callback.invoke(body.apply { responseCode = response.code() })
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                callback.invoke(
                    TracksResponse(ArrayList()).apply { responseCode = 400 }
                )
            }
        })
    }
}