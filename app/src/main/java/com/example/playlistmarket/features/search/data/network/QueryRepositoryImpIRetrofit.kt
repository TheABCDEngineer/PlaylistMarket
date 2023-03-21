package com.example.playlistmarket.features.search.data.network

import com.example.playlistmarket.features.search.data.dto.Response
import com.example.playlistmarket.features.search.data.dto.TracksRequest
import com.example.playlistmarket.features.search.data.dto.TracksResponse
import com.example.playlistmarket.features.search.domain.model.ResponseModel
import com.example.playlistmarket.features.search.domain.repository.QueryRepository
import retrofit2.Call
import retrofit2.Callback

class QueryRepositoryImpIRetrofit(apiService: ApiService) : QueryRepository {

    override lateinit var callback: (ResponseModel) -> Unit
    private val serviceApi = apiService.createApiService()

    override fun executeRequest(queryValue: String) {
        val request = TracksRequest(queryValue)
        serviceApi.findTrack(request.expression).enqueue(
            object :
                Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: retrofit2.Response<TracksResponse>
                ) {
                    val body = response.body() ?: Response()

                    callback.invoke(
                        ResponseConverter.convertToDomain(
                            body.apply {
                                responseCode = response.code()
                            }
                        )
                    )
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    callback.invoke(
                        ResponseConverter.convertToDomain(
                            TracksResponse(ArrayList()).apply { responseCode = 400 }
                        )
                    )
                }
            }
        )
    }
}