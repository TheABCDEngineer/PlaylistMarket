package com.example.playlistmarket.search.widgets

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.playlistmarket.R
import com.example.playlistmarket.search.SearchActivity
import com.example.playlistmarket.search.query.ResponseHandle

class QueryStatusWidget(
    private val activity: SearchActivity,
    private val requestStatusImage: ImageView,
    private val requestStatusMessage: TextView,
    private val progressBar: ProgressBar,
    val refreshButton: Button
) {
    init {
        hideQueryPlaceholder()
        refreshButton.setOnClickListener {
            if (refreshButton.text == activity.getString(R.string.search_refresh_button_title)) activity.startTracksSearchingOnQuery()
            if (refreshButton.text == activity.getString(R.string.clear_history_button)) activity.clearSearchingHistory()
        }
    }

    fun prepareToShowQueryPlaceholder(status: ResponseHandle) {
        requestStatusImage.setImageDrawable(status.image)
        requestStatusImage.isVisible = !status.isProgressBar
        requestStatusMessage.text = status.message
        requestStatusMessage.isVisible = !status.isProgressBar
        refreshButton.isVisible = status.isRefreshButton
        progressBar.isVisible = status.isProgressBar

        if (status.isRefreshButton) {
            refreshButton.text = activity.getString(R.string.search_refresh_button_title)
        }
    }

    fun hideQueryPlaceholder() {
        requestStatusImage.visibility = View.GONE
        requestStatusMessage.visibility = View.GONE
        refreshButton.visibility = View.GONE
        progressBar.isVisible = false
    }

    fun prepareToShowHistory() {
        refreshButton.text = activity.getString(R.string.clear_history_button)
        refreshButton.visibility = View.VISIBLE
    }
}