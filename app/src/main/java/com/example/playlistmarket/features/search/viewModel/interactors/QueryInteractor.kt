package com.example.playlistmarket.features.search.viewModel.interactors

import com.example.playlistmarket.creator.observe.Observable

interface QueryInteractor : Observable {
    fun executeQuery(queryValue: String)
}