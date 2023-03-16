package com.example.playlistmarket.features.search.presentation.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmarket.R
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.presentation.ui.recycler.SearchTrackAdapter
import com.example.playlistmarket.creator.hideKeyboard
import com.example.playlistmarket.features.search.presentation.ui.widgets.ScreenStateWidget
import com.example.playlistmarket.features.search.presentation.ui.widgets.SearchingWidget

class SearchActivity : AppCompatActivity() {
    private val goBackButton: ImageView by lazy { findViewById(R.id.search_goBack) }
    private val searchingWidget: SearchingWidget by lazy { SearchingWidget(this) }
    private val screenStateWidget: ScreenStateWidget by lazy { ScreenStateWidget(this) }

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]
    }

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