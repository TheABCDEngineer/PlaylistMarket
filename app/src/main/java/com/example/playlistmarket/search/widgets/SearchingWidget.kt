package com.example.playlistmarket.search.widgets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.search.SearchActivity
import com.example.playlistmarket.search.query.ItunesApi
import com.example.playlistmarket.search.query.ResponseHandle
import com.example.playlistmarket.search.query.SearchQuery

class SearchingWidget(
    private val activity: SearchActivity,
    val editText: EditText,
    val clearButton: ImageView,
    savedInstanceState: Bundle?
) {
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val searchRunnable = Runnable { initializeQuery() }

    init {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                activity.hideKeyboard()
            }
            false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            editText.hint =
                if (hasFocus && editText.text.isEmpty()) activity.getString(R.string.search) else ""
        }

        clearButton.setOnClickListener {
            editText.setText("")
            clearButton.visibility = View.GONE
            activity.clearSearchingConditions()
        }
        if (savedInstanceState != null) {
            editText.setText(
                savedInstanceState.getString(
                    App.SEARCH_CONTENT_EDIT_TEXT_KEY,
                    ""
                )
            )
        }
    }

    private fun searchDebounce() {
        App.mainHandler.removeCallbacks(searchRunnable)
        if (editText.text.isNullOrEmpty()) return
        App.mainHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun initializeQuery() {
        activity.showQueryPlaceholder(ResponseHandle.SEARCHING)
        App.searchQuery = SearchQuery(App.SEARCH_TRACKS_BASE_URL, ItunesApi::class.java)
        App.searchQuery!!.addObserver(activity)
        App.searchQuery!!.executeTracksQuery(editText.text.toString())
    }

    fun clearQuery() {
        App.searchQuery = null
    }

    fun resumeQuery(): QueryStatus {
        if (App.searchQuery != null) {
            App.searchQuery!!.addObserver(activity)

            if (App.searchQuery!!.isQueryExecuted) {
                return QueryStatus.TRUE_RESULTS_EXIST
            }
            return QueryStatus.TRUE_NO_RESULTS
        }
        return QueryStatus.FALSE
    }

    fun executeQueryNotifyObserver() {
        App.searchQuery!!.executeNotifyObserver()
    }
}

enum class QueryStatus {
    FALSE, TRUE_NO_RESULTS, TRUE_RESULTS_EXIST
}