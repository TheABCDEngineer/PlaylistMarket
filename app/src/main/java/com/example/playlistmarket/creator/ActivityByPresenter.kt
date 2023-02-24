package com.example.playlistmarket.creator

import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.creator.Presenter

abstract class ActivityByPresenter: AppCompatActivity() {
    abstract val presenter: Presenter

    abstract fun createPresenter()

    override fun onResume() {
        super.onResume()
        presenter.onViewResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onViewBackPressed()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onViewDestroy()
    }
}