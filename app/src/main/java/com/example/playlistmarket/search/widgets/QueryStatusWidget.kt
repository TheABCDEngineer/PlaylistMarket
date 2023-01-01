package com.example.playlistmarket.search.widgets

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.playlistmarket.R
import com.example.playlistmarket.search.SearchActivity
import com.example.playlistmarket.search.query.ResponseHandle

class QueryStatusWidget(
    private val activity: SearchActivity,
    statusImageId: Int,
    statusMessageId: Int,
    refreshButtonId: Int
) {
    private val requestStatusImage: ImageView = activity.findViewById(statusImageId)
    private val requestStatusMessage: TextView = activity.findViewById(statusMessageId)
    val refreshButton: Button = activity.findViewById(refreshButtonId)

    init {
        refreshButton.setOnClickListener {
            if (refreshButton.text == activity.getString(R.string.search_refresh_button_title)) activity.startTracksSearchingOnQuery()
            if (refreshButton.text == activity.getString(R.string.clear_history_button)) activity.clearSearchingHistory()
        }
    }

    fun prepareToShowQueryPlaceholder(status: ResponseHandle) {
        requestStatusImage.setImageDrawable(status.image)
        requestStatusImage.visibility = View.VISIBLE
        requestStatusMessage.text = status.message
        requestStatusMessage.visibility = View.VISIBLE
        refreshButton.visibility = View.GONE

        if (status.isRefreshButton) {
            refreshButton.text = activity.getString(R.string.search_refresh_button_title)
            refreshButton.visibility = View.VISIBLE
        }
    }

    fun hideQueryPlaceholder() {
        requestStatusImage.visibility = View.GONE
        requestStatusMessage.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    fun prepareToShowHistory() {
        refreshButton.text = activity.getString(R.string.clear_history_button)
        refreshButton.visibility = View.VISIBLE
    }
}