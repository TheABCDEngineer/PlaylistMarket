package com.example.playlistmarket.data.interactors

import com.example.playlistmarket.creator.interactors.Observable
import com.example.playlistmarket.creator.interactors.Observer

interface QueryInteractor : Observable {
    override fun addObserver(observer: Observer)
    fun executeQuery(queryValue: String?)
    fun executeCallback()
}