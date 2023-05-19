package com.example.playlistmarket.features.search.presentation.ui.widgets

import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.playlistmarket.databinding.FragmentSearchBinding
import com.example.playlistmarket.features.search.domain.enums.FunctionalButtonMode
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.presentation.ui.recyclerView.SearchTrackAdapter

class ScreenStateWidget(
    binding: FragmentSearchBinding
) {
    private val requestStatusImage = binding.requestStatusImage
    private val requestStatusMessage = binding.requestStatusMessage
    private val progressBar = binding.progressBar
    private val functionalButton = binding.functionalButton
    private val feed = binding.trackFeed
    private val recyclerLayout = binding.recyclerLayout
    private val title = binding.recentTracksTitle
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