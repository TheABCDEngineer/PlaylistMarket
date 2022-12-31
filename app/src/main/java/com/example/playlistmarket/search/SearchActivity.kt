package com.example.playlistmarket.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.App
import com.example.playlistmarket.Observer
import com.example.playlistmarket.R
import com.example.playlistmarket.Track
import com.example.playlistmarket.TrackListHandler
import com.example.playlistmarket.search.query.ItunesApi
import com.example.playlistmarket.search.query.ResponseHandle
import com.example.playlistmarket.search.query.SearchQuery
import com.example.playlistmarket.search.recycler.SearchTrackAdapter

class SearchActivity : AppCompatActivity(), Observer {
    private lateinit var searchContentEditText: EditText
    private lateinit var searchContentClearButton: ImageView
    private lateinit var goBackButton: ImageView
    private lateinit var trackListRecycler: RecyclerView
    private lateinit var requestStatusImage: ImageView
    private lateinit var requestStatusMessage: TextView
    private lateinit var refreshButton: Button
    private lateinit var recentTitle: TextView
    private lateinit var recyclerLayout: LinearLayout

    private lateinit var historyAdapter: SearchTrackAdapter
    private lateinit var searchHistory: TrackListHandler

    private var queryTracksList = ArrayList<Track>()
    private val queryAdapter = SearchTrackAdapter(queryTracksList)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            App.SEARCH_CONTENT_EDIT_TEXT_KEY,
            searchContentEditText.text.toString()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        initializeVariables()

        hideQueryPlaceholder()

        setOnClickListenersAtViews()

        showStartScreen(savedInstanceState)
    }

    private fun initializeVariables() {
        searchContentEditText = findViewById(R.id.search_EditText)
        searchContentClearButton = findViewById(R.id.search_ClearButton)
        goBackButton = findViewById(R.id.search_goBack)
        requestStatusImage = findViewById(R.id.search_request_status_image)
        requestStatusMessage = findViewById(R.id.search_request_status_text)
        refreshButton = findViewById(R.id.search_refresh_button)
        trackListRecycler = findViewById(R.id.search_track_list)
        recentTitle = findViewById(R.id.recent_tracks_title)
        recyclerLayout = findViewById(R.id.recycler_layout)

        val searchContentEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    searchContentClearButton.visibility = View.GONE
                } else {
                    searchContentClearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchContentEditText.addTextChangedListener(searchContentEditTextWatcher)

        searchHistory = TrackListHandler(App.sharedPref, App.RECENT_TRACKS_LIST_KEY, 10)

        queryAdapter.addObserver(searchHistory)
        historyAdapter = SearchTrackAdapter(searchHistory.items)
        historyAdapter.addObserver(searchHistory)

        recentTitle.visibility = View.GONE
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }

        refreshButton.setOnClickListener {
            if (refreshButton.text == getString(R.string.search_refresh_button_title)) startTracksSearchingOnQuery()
            if (refreshButton.text == getString(R.string.clear_history_button)) clearSearchingHistory()
        }

        searchContentClearButton.setOnClickListener {
            searchContentEditText.setText("")
            searchContentClearButton.visibility = View.GONE
            trackListRecycler.visibility = View.GONE
            App.itunesSearchQuery = null
            hideQueryPlaceholder()
            hideKeyboard()
            showHistory()
        }

        searchContentEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startTracksSearchingOnQuery()
            }
            false
        }

        searchContentEditText.setOnFocusChangeListener { _, hasFocus ->
            searchContentEditText.hint =
                if (hasFocus && searchContentEditText.text.isEmpty()) getString(R.string.search) else ""
        }
    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showQueryPlaceholder(status: ResponseHandle) {
        recyclerLayout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        recyclerLayout.layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = 0F
        }
        trackListRecycler.visibility = View.GONE
        requestStatusImage.setImageDrawable(status.image)
        requestStatusImage.visibility = View.VISIBLE
        requestStatusMessage.text = status.message
        requestStatusMessage.visibility = View.VISIBLE
        refreshButton.visibility = View.GONE

        if (status.isRefreshButton) {
            refreshButton.text = getString(R.string.search_refresh_button_title)
            refreshButton.visibility = View.VISIBLE
        }
    }

    private fun hideQueryPlaceholder() {
        requestStatusImage.visibility = View.GONE
        requestStatusMessage.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun startTracksSearchingOnQuery() {
        if (searchContentEditText.text.isEmpty()) return
        recentTitle.visibility = View.GONE
        showQueryPlaceholder(ResponseHandle.SEARCHING)

        trackListRecycler.adapter = queryAdapter

        App.itunesSearchQuery = SearchQuery(App.SEARCH_TRACKS_BASE_URL, ItunesApi::class.java)
        App.itunesSearchQuery!!.addObserver(this@SearchActivity)
        App.itunesSearchQuery!!.executeTracksQuery(searchContentEditText.text.toString())
    }

    private fun showHistory() {
        recyclerLayout.layoutParams.height = 0
        recyclerLayout.layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = 1F
        }
        recentTitle.visibility = View.VISIBLE
        refreshButton.text = getString(R.string.clear_history_button)
        refreshButton.visibility = View.VISIBLE

        trackListRecycler.adapter = historyAdapter
        trackListRecycler.adapter!!.notifyDataSetChanged()
        trackListRecycler.visibility = View.VISIBLE
    }

    private fun clearSearchingHistory() {
        recentTitle.visibility = View.GONE
        refreshButton.visibility = View.GONE
        trackListRecycler.visibility = View.GONE
        searchHistory.clear()
    }

    private fun showQueryResults(trackList: ArrayList<Track>, error: ResponseHandle?) {
        if (error != null) {
            showQueryPlaceholder(error)
            return
        }
        queryTracksList.clear()
        queryTracksList.addAll(trackList)
        trackListRecycler.adapter!!.notifyDataSetChanged()

        hideQueryPlaceholder()
        trackListRecycler.visibility = View.VISIBLE
    }

    override fun <S, T> notifyObserver(event: S?, data: T) {
        showQueryResults(
            data as ArrayList<Track>,
            event as ResponseHandle?
        )
    }

    override fun onResume() {
        super.onResume()
        if (trackListRecycler.adapter == historyAdapter) {
            trackListRecycler.adapter!!.notifyDataSetChanged()
        }
    }

    private fun showStartScreen(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            searchContentEditText.setText(
                savedInstanceState.getString(
                    App.SEARCH_CONTENT_EDIT_TEXT_KEY,
                    ""
                )
            )
        }
        if (App.itunesSearchQuery != null) {
            App.itunesSearchQuery!!.addObserver(this@SearchActivity)

            if (App.itunesSearchQuery!!.isQueryExecuted) {
                trackListRecycler.adapter = queryAdapter
                App.itunesSearchQuery!!.executeNotifyObserver()
                return
            }
            showQueryPlaceholder(ResponseHandle.SEARCHING)
            return
        }

        if (searchHistory.items.size > 0) {
            showHistory()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        App.itunesSearchQuery = null
    }
}