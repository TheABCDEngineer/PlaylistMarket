package com.example.playlistmarket

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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

    private lateinit var searchContentEditTextValue: String
    private lateinit var searchContentEditText: EditText
    private lateinit var searchContentClearButton: ImageView
    private lateinit var goBackButton: ImageView
    private lateinit var queryTrackList: RecyclerView
    private lateinit var requestStatusImage: ImageView
    private lateinit var requestStatusMessage: TextView
    private lateinit var refreshButton: Button

    private var searchItemsList = ArrayList<Track>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SEARCH_CONTENT_EDIT_TEXT,
            searchContentEditTextValue
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchContentEditTextValue = savedInstanceState.getString(SEARCH_CONTENT_EDIT_TEXT, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        searchContentEditTextValue = ""
        searchContentEditText = findViewById(R.id.search_EditText)
        searchContentClearButton = findViewById(R.id.search_ClearButton)
        goBackButton = findViewById(R.id.search_goBack)
        requestStatusImage = findViewById(R.id.search_request_status_image)
        requestStatusMessage = findViewById(R.id.search_request_status_text)
        refreshButton = findViewById(R.id.search_refresh_button)
        queryTrackList = findViewById(R.id.search_track_list)

        queryTrackList.adapter = SearchTrackAdapter(searchItemsList)

        searchContentEditText.setText(searchContentEditTextValue)

        hideQueryPlaceholder()

        goBackButton.setOnClickListener {
            onBackPressed()
        }

        refreshButton.setOnClickListener {
            tracksSearchOnQuery()
        }

        searchContentClearButton.setOnClickListener {
            searchContentEditText.setText("")
            searchContentClearButton.visibility = View.GONE
            queryTrackList.visibility = View.GONE
            hideKeyboard()
        }

        searchContentEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                tracksSearchOnQuery()
            }
            false
        }

        val searchContentEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    searchContentClearButton.visibility = View.GONE
                } else {
                    searchContentEditTextValue = s.toString()
                    searchContentClearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchContentEditText.addTextChangedListener(searchContentEditTextWatcher)

    }

    private fun hideKeyboard() {
        this.currentFocus?.let { view ->
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showQueryPlaceholder(image: Int, message: Int, isRefreshButton: Boolean) {
        queryTrackList.visibility = View.GONE
        requestStatusImage.setImageResource(image)
        requestStatusImage.visibility = View.VISIBLE
        requestStatusMessage.setText(message)
        requestStatusMessage.visibility = View.VISIBLE

        if (isRefreshButton) {
            refreshButton.visibility = View.VISIBLE
        }
    }

    private fun hideQueryPlaceholder() {
        requestStatusImage.visibility = View.GONE
        requestStatusMessage.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun tracksSearchOnQuery() {
        showQueryPlaceholder(R.drawable.please_wait_icon, R.string.search_status_please_wait, false)
        if (searchContentEditText.text.isNotEmpty()) {
            itunesService.findTrack(searchContentEditText.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        searchItemsList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            hideQueryPlaceholder()
                            searchItemsList.addAll(response.body()?.results!!)
                            queryTrackList.adapter!!.notifyDataSetChanged()
                            queryTrackList.visibility = View.VISIBLE
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

}