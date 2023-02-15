package com.example.playlistmarket.ui

import com.example.playlistmarket.controller.Controller

interface ControlledActivity {
    val controller: Controller

    val destroyController: () -> Unit
        get() = { controller.implementControllerDestroyingMethod() }

    fun createController()

    fun onResume() {
        controller.onViewResume()
    }

    fun onBackPressed() {
        controller.onViewBackPressed()
    }

    fun onStop() {
        controller.onViewStop()
    }

    fun onDestroy() {
        controller.onViewDestroy(destroyController)
    }
}