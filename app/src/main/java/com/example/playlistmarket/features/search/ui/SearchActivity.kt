package com.example.playlistmarket.features.search.ui

import android.os.Bundle
import android.widget.ImageView
import com.example.playlistmarket.R
import com.example.playlistmarket.features.search.presenter.SearchPresenter
import com.example.playlistmarket.features.search.presenter.SearchView
import com.example.playlistmarket.features.search.presenter.enums.SearchScreenState
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.features.search.ui.recycler.SearchTrackAdapter
import com.example.playlistmarket.creator.hideKeyboard
import com.example.playlistmarket.creator.ActivityByPresenter
import com.example.playlistmarket.features.search.ui.widgets.ScreenStateWidget
import com.example.playlistmarket.features.search.ui.widgets.SearchingWidget


class SearchActivity : ActivityByPresenter(), SearchView {
    private val goBackButton: ImageView by lazy { findViewById(R.id.search_goBack) }
    private val searchingWidget: SearchingWidget by lazy { SearchingWidget(this) }
    private val screenStateWidget: ScreenStateWidget by lazy { ScreenStateWidget(this) }
    override val presenter: SearchPresenter by lazy { Creator.searchPresenter!! }

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

        createPresenter()

        val initialText = savedInstanceState?.getString(
            SEARCH_CONTENT_EDIT_TEXT_KEY, ""
        ) ?: ""

        searchingWidget.apply {
            onUserRequestTextChange = { value ->
                presenter.onUserRequestTextChange(value)
            }
            clearSearchingConditions = {
                presenter.currentScreenState = null
                presenter.onViewResume()
            }
            hideSearchingKeyboard = {
                hideKeyboard(this@SearchActivity)
            }
            initializeSearchingSettings(initialText)
        }

        screenStateWidget.apply {
            onFunctionalButtonClick = { mode ->
                presenter.onFunctionalButtonPressed(mode)
            }
            setCurrentScreenState = { state ->
                presenter.currentScreenState = state
            }
        }

        goBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun createPresenter() {
        Creator.createSearchPresenter(this)
    }

    override fun updateScreenState(state: SearchScreenState) {
        screenStateWidget.setScreenState(state)
    }

    override fun updateTrackFeed(adapter: SearchTrackAdapter) {
        screenStateWidget.setTrackFeed(adapter)
    }
}