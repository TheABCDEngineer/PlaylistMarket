package com.example.playlistmarket.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.QueryStatusObserver
import com.example.playlistmarket.R
import com.example.playlistmarket.Track
import com.example.playlistmarket.search.query.ResponseHandle
import com.example.playlistmarket.search.query.SearchQuery

class SearchActivity : AppCompatActivity(), QueryStatusObserver {
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
    private lateinit var searchHistory: SearchHistory

    private var queryTracksList = ArrayList<Track>()
    private val queryAdapter = SearchTrackAdapter(queryTracksList)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            getString(R.string.search_content_edit_text_key),
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

        if (savedInstanceState != null) {
            searchContentEditText.setText(
                savedInstanceState.getString(
                    getString(R.string.search_content_edit_text_key),
                    ""
                )
            )
        }

        if (searchHistory.recentTracksList.size > 0) {
            showHistory()
        }

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
    }

    override fun onStop() {
        super.onStop()
        searchHistory.saveToFile()
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

        val fileName = getString(R.string.app_preference_file_name)
        val recentTracksListKey = getString(R.string.recent_tracks_list_key)
        val sharedPrefs = getSharedPreferences(fileName, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs, recentTracksListKey)
        searchHistory.loadFromFile()

        queryAdapter.addObserver(searchHistory)
        historyAdapter = SearchTrackAdapter(searchHistory.recentTracksList)
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

        val searchQuery = SearchQuery()
        searchQuery.addObserver(this@SearchActivity)
        searchQuery.executeTracksQuery(searchContentEditText.text.toString())
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
        searchHistory.clearHistory()
    }

    override fun showQueryResults(trackList: ArrayList<Track>, error: ResponseHandle?) {
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
}