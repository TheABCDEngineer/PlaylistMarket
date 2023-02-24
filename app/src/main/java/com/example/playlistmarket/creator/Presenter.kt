package com.example.playlistmarket.creator

import com.example.playlistmarket.App

abstract class Presenter {
    var isDestroyAllowed = false
    private val setDestroyAllowed = Runnable { isDestroyAllowed = true }

    abstract fun implementPresenterDestroyingMethod(): () -> Unit

    open fun onViewResume() {
        App.mainHandler.removeCallbacks(setDestroyAllowed)
        isDestroyAllowed = false
    }

    open fun onViewBackPressed() {
        isDestroyAllowed = true
    }

    open fun onViewStop() {
        if (!isDestroyAllowed) App.mainHandler.postDelayed(setDestroyAllowed, 5000L)
    }

    open fun onViewDestroy() {
        val destroyPresenter = implementPresenterDestroyingMethod()
        if (isDestroyAllowed) destroyPresenter.invoke()
    }
}