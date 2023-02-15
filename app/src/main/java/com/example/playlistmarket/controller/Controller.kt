package com.example.playlistmarket.controller

import com.example.playlistmarket.creator.App

abstract class Controller {
    var isDestroyAllowed = false
    private val setDestroyAllowed = Runnable { isDestroyAllowed = true }

    abstract fun implementControllerDestroyingMethod(): () -> Unit

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

    open fun onViewDestroy(destroyController: () -> Unit) {
        if (isDestroyAllowed) destroyController.invoke()
    }
}