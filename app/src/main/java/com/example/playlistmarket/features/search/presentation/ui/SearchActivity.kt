package com.example.playlistmarket.features.search.presentation.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.R
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.presentation.ui.recycler.SearchTrackAdapter
import com.example.playlistmarket.base.hideKeyboard
import com.example.playlistmarket.features.search.presentation.ui.widgets.ScreenStateWidget
import com.example.playlistmarket.features.search.presentation.ui.widgets.SearchingWidget
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private val goBackButton: ImageView by lazy { findViewById(R.id.search_goBack) }
    private val searchingWidget: SearchingWidget by lazy { SearchingWidget(this) }
    private val screenStateWidget: ScreenStateWidget by lazy { ScreenStateWidget(this) }
    private val viewModel by viewModel<SearchViewModel>()

    companion object {
        private const val SEARCH_CONTENT_EDIT_TEXT_KEY = "searching_edit_text"
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SEARCH_CONTENT_EDIT_TEXT_KEY,
            searchingWidget.getRequestTextValue()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        viewModel.observeScreenState().observe(this) {
            updateScreenState(it)
        }
        viewModel.observeTrackFeedState().observe(this) {
            updateTrackFeed(it)
        }

        val initialText = savedInstanceState?.getString(
            SEARCH_CONTENT_EDIT_TEXT_KEY, ""
        ) ?: ""

        searchingWidget.apply {
            onUserRequestTextChange = { value ->
                viewModel.onUserRequestTextChange(value)
            }
            clearSearchingConditions = {
                viewModel.setStartScreen()
            }
            hideSearchingKeyboard = {
                hideKeyboard(this@SearchActivity)
            }
            initializeSearchingSettings(initialText)
        }

        screenStateWidget.apply {
            onFunctionalButtonClick = { mode ->
                viewModel.onFunctionalButtonPressed(mode)
            }
        }

        goBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun updateScreenState(state: SearchScreenState) {
        screenStateWidget.setScreenState(state)
    }

    private fun updateTrackFeed(adapter: SearchTrackAdapter) {
        screenStateWidget.setTrackFeed(adapter)
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateHistoryState()
    }
}