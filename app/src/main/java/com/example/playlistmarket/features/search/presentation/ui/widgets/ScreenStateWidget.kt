package com.example.playlistmarket.features.search.presentation.ui.widgets

import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.features.search.domain.enums.FunctionalButtonMode
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.presentation.ui.recyclerView.SearchTrackAdapter
import com.example.playlistmarket.features.search.presentation.ui.SearchActivity


class ScreenStateWidget(
    activity: SearchActivity
) {
    private val requestStatusImage: ImageView =
        activity.findViewById(R.id.search_request_status_image)
    private val requestStatusMessage: TextView =
        activity.findViewById(R.id.search_request_status_text)
    private val progressBar: ProgressBar = activity.findViewById(R.id.search_progressBar)
    private val functionalButton: Button = activity.findViewById(R.id.search_refresh_button)
    private val feed: RecyclerView = activity.findViewById(R.id.search_track_list)
    private val recyclerLayout: LinearLayout = activity.findViewById(R.id.recycler_layout)
    private val title: TextView = activity.findViewById(R.id.recent_tracks_title)
    private lateinit var functionalButtonMode: FunctionalButtonMode

    lateinit var onFunctionalButtonClick: (FunctionalButtonMode) -> Unit

    init {
        functionalButton.setOnClickListener {
            onFunctionalButtonClick.invoke(functionalButtonMode)
        }
    }

    fun setScreenState(state: SearchScreenState) {
        title.text = state.title
        requestStatusImage.setImageDrawable(state.image)
        requestStatusMessage.text = state.message
        functionalButton.text = state.functionalButton.title
        functionalButtonMode = state.functionalButton

        title.isVisible = state.isTitle
        feed.isVisible = state.isFeed
        requestStatusImage.isVisible = !state.isFeed
        requestStatusMessage.isVisible = !state.isFeed
        functionalButton.isVisible = state.isFunctionalButton
        progressBar.isVisible = state.isProgressBar

        recyclerLayout.layoutParams.height = state.functionalButton.layoutHeight
        recyclerLayout.layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = state.functionalButton.layoutWeight
        }
    }

    fun setTrackFeed(adapter: SearchTrackAdapter) {
        feed.adapter = adapter
        feed.adapter!!.notifyDataSetChanged()
    }
}