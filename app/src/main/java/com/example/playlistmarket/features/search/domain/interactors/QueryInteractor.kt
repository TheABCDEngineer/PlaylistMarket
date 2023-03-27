package com.example.playlistmarket.features.search.domain.interactors

import com.example.playlistmarket.base.observe.Observable

interface QueryInteractor : Observable {
    fun executeQuery(queryValue: String)
}