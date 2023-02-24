package com.example.playlistmarket.data.repository

import com.example.playlistmarket.creator.observe.Observable

interface QueryRepository : Observable {
    //override fun addObserver(observer: Observer)
    fun executeQuery(queryValue: String?)
    fun executeCallback()
}