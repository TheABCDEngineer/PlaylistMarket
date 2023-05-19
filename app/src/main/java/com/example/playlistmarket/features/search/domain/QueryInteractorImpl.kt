package com.example.playlistmarket.features.search.domain

import com.example.playlistmarket.features.search.domain.interactors.QueryInteractor
import com.example.playlistmarket.root.observe.Observer
import com.example.playlistmarket.features.search.domain.repository.QueryRepository

class QueryInteractorImpl(
    private val networkClient: QueryRepository
) : QueryInteractor {

    private lateinit var observer: Observer

    init {
        networkClient.callback = {
            observer.notifyObserver(
                it.error,
                it.resultTrackList
            )
        }
    }

    override fun addObserver(observer: Observer) {
        this.observer = observer
    }

    override fun executeQuery(queryValue: String) {
        networkClient.executeRequest(queryValue)
    }
}