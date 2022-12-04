package com.example.playlistmarket

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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_CONTENT_EDIT_TEXT = "searchContentEditText"
    }

    private val searchBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private lateinit var searchContentEditText: EditText
    private lateinit var searchContentClearButton: ImageView
    private lateinit var goBackButton: ImageView
    private lateinit var trackListRecycler: RecyclerView
    private lateinit var requestStatusImage: ImageView
    private lateinit var requestStatusMessage: TextView
    private lateinit var refreshButton: Button
    private lateinit var recentTitle: TextView
    private lateinit var recyclerLayout: LinearLayout

    private lateinit var queryAdapter: SearchTrackAdapter
    private lateinit var historyAdapter: SearchTrackAdapter
    private lateinit var searchHistory: SearchHistory

    private var queryTracksList = ArrayList<Track>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SEARCH_CONTENT_EDIT_TEXT,
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
                    SEARCH_CONTENT_EDIT_TEXT,
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

        queryAdapter = SearchTrackAdapter(queryTracksList)
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
            if (refreshButton.text == getString(R.string.search_refresh_button_title)) searchTracksOnQuery()
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
                searchTracksOnQuery()
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

    private fun showQueryPlaceholder(image: Int, message: Int, isRefreshButton: Boolean) {
        recyclerLayout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        recyclerLayout.layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = 0F
        }
        trackListRecycler.visibility = View.GONE
        requestStatusImage.setImageResource(image)
        requestStatusImage.visibility = View.VISIBLE
        requestStatusMessage.setText(message)
        requestStatusMessage.visibility = View.VISIBLE
        refreshButton.visibility = View.GONE

        if (isRefreshButton) {
            refreshButton.text = getString(R.string.search_refresh_button_title)
            refreshButton.visibility = View.VISIBLE
        }
    }

    private fun hideQueryPlaceholder() {
        requestStatusImage.visibility = View.GONE
        requestStatusMessage.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun searchTracksOnQuery() {
        recentTitle.visibility = View.GONE
        showQueryPlaceholder(R.drawable.please_wait_icon, R.string.search_status_please_wait, false)
        trackListRecycler.adapter = queryAdapter

        if (searchContentEditText.text.isNotEmpty()) {
            itunesService.findTrack(searchContentEditText.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        queryTracksList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            hideQueryPlaceholder()
                            queryTracksList.addAll(response.body()?.results!!)
                            trackListRecycler.adapter!!.notifyDataSetChanged()
                            trackListRecycler.visibility = View.VISIBLE
                        } else {
                            showQueryPlaceholder(
                                R.drawable.no_any_content,
                                R.string.search_request_status_text_if_no_results,
                                false
                            )
                        }
                    } else {
                        showQueryPlaceholder(
                            R.drawable.warning_icon,
                            R.string.search_request_status_text_if_server_error,
                            true
                        )
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    showQueryPlaceholder(
                        R.drawable.no_internet_connection,
                        R.string.search_request_status_text_if_no_internet,
                        true
                    )
                }

            })
        }
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
}