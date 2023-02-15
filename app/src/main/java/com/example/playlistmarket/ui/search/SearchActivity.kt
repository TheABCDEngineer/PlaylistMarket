package com.example.playlistmarket.ui.search

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.R
import com.example.playlistmarket.controller.search.SearchController
import com.example.playlistmarket.controller.search.SearchView
import com.example.playlistmarket.controller.search.enums.SearchScreenState
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.controller.search.recycler.SearchTrackAdapter
import com.example.playlistmarket.creator.hideKeyboard
import com.example.playlistmarket.ui.ControlledActivity
import com.example.playlistmarket.ui.search.widgets.ScreenStateWidget
import com.example.playlistmarket.ui.search.widgets.SearchingWidget


class SearchActivity : AppCompatActivity(), SearchView, ControlledActivity {
    private val goBackButton: ImageView by lazy { findViewById(R.id.search_goBack) }
    private val searchingWidget: SearchingWidget by lazy { SearchingWidget(this) }
    private val screenStateWidget: ScreenStateWidget by lazy { ScreenStateWidget(this) }
    override val controller: SearchController by lazy { Creator.searchController!! }

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

        createController()

        val initialText = savedInstanceState?.getString(
            SEARCH_CONTENT_EDIT_TEXT_KEY, ""
        ) ?: ""

        searchingWidget.apply {
            onUserRequestTextChange = { value ->
                controller.onUserRequestTextChange(value)
            }
            clearSearchingConditions = {
                controller.currentScreenState = null
                controller.onViewResume()
            }
            hideSearchingKeyboard = {
                hideKeyboard(this@SearchActivity)
            }
            initializeSearchingSettings(initialText)
        }

        screenStateWidget.apply {
            onFunctionalButtonClick = { mode ->
                controller.onFunctionalButtonPressed(mode)
            }
            setCurrentScreenState = { state ->
                controller.currentScreenState = state
            }
        }

        goBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun createController() {
        Creator.createSearchController(this)
    }

    override fun updateScreenState(state: SearchScreenState) {
        screenStateWidget.setScreenState(state)
    }

    override fun updateTrackFeed(adapter: SearchTrackAdapter) {
        screenStateWidget.setTrackFeed(adapter)
    }

    override fun onResume() {
        super<AppCompatActivity>.onResume()
        super<ControlledActivity>.onResume()
    }

    override fun onBackPressed() {
        super<ControlledActivity>.onBackPressed()
        super<AppCompatActivity>.onBackPressed()
    }

    override fun onStop() {
        super<ControlledActivity>.onStop()
        super<AppCompatActivity>.onStop()
    }

    override fun onDestroy() {
        super<ControlledActivity>.onDestroy()
        super<AppCompatActivity>.onDestroy()
    }
}