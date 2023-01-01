package com.example.playlistmarket.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmarket.App
import com.example.playlistmarket.Observer
import com.example.playlistmarket.R
import com.example.playlistmarket.Track
import com.example.playlistmarket.search.query.ResponseHandle
import com.example.playlistmarket.search.widgets.QueryStatus
import com.example.playlistmarket.search.widgets.QueryStatusWidget
import com.example.playlistmarket.search.widgets.RecyclerViewWidget
import com.example.playlistmarket.search.widgets.SearchingWidget

class SearchActivity : AppCompatActivity(), Observer {
    private lateinit var goBackButton: ImageView
    private lateinit var recyclerWidget: RecyclerViewWidget
    private lateinit var queryStatusWidget: QueryStatusWidget
    private lateinit var searchingWidget: SearchingWidget

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            App.SEARCH_CONTENT_EDIT_TEXT_KEY,
            searchingWidget.editText.text.toString()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        initializeVariables(savedInstanceState)

        queryStatusWidget.hideQueryPlaceholder()

        setOnClickListenersAtViews()

        showStartScreen()
    }

    private fun initializeVariables(savedInstanceState: Bundle?) {
        goBackButton = findViewById(R.id.search_goBack)

        recyclerWidget = RecyclerViewWidget(
            this@SearchActivity,
            R.id.search_track_list,
            R.id.recycler_layout,
            R.id.recent_tracks_title
        )
        queryStatusWidget = QueryStatusWidget(
            this@SearchActivity,
            R.id.search_request_status_image,
            R.id.search_request_status_text,
            R.id.search_refresh_button
        )
        searchingWidget = SearchingWidget(
            this@SearchActivity,
            R.id.search_EditText,
            R.id.search_ClearButton,
            savedInstanceState
        )
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    fun clearSearchingConditions() {
        recyclerWidget.recycler.visibility = View.GONE
        searchingWidget.clearQuery()
        queryStatusWidget.hideQueryPlaceholder()
        hideKeyboard()
        showHistory()
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showQueryPlaceholder(status: ResponseHandle) {
        recyclerWidget.prepareToShowQueryPlaceholder()
        queryStatusWidget.prepareToShowQueryPlaceholder(status)
    }

    fun startTracksSearchingOnQuery() {
        showQueryPlaceholder(ResponseHandle.SEARCHING)
        searchingWidget.initializeQuery(this@SearchActivity)
    }

    private fun showHistory() {
        if (recyclerWidget.searchHistory.items.size == 0) return
        recyclerWidget.prepareToShowHistory()
        queryStatusWidget.prepareToShowHistory()
    }

    fun clearSearchingHistory() {
        queryStatusWidget.refreshButton.visibility = View.GONE
        recyclerWidget.clearHistory()
    }

    private fun showQueryResults(trackList: ArrayList<Track>, error: ResponseHandle?) {
        if (error != null) {
            showQueryPlaceholder(error)
            return
        }
        queryStatusWidget.hideQueryPlaceholder()
        recyclerWidget.prepareToShowQueryResults(trackList)
    }

    override fun <S, T> notifyObserver(event: S?, data: T) {
        showQueryResults(
            data as ArrayList<Track>,
            event as ResponseHandle?
        )
    }

    override fun onResume() {
        super.onResume()
        recyclerWidget.onActivityResume()
    }

    private fun showStartScreen() {
        when (searchingWidget.resumeQuery(this@SearchActivity)) {
            QueryStatus.FALSE -> showHistory()
            QueryStatus.TRUE_NO_RESULTS -> showQueryPlaceholder(ResponseHandle.SEARCHING)
            QueryStatus.TRUE_RESULTS_EXIST -> {
                recyclerWidget.recycler.adapter = recyclerWidget.queryAdapter
                searchingWidget.executeQueryNotifyObserver()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        searchingWidget.clearQuery()
    }
}