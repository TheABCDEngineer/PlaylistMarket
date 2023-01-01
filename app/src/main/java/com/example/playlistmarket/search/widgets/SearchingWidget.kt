package com.example.playlistmarket.search.widgets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.search.SearchActivity
import com.example.playlistmarket.search.query.ItunesApi
import com.example.playlistmarket.search.query.SearchQuery

class SearchingWidget(
    activity: SearchActivity,
    editTextId: Int,
    contentClearButton: Int,
    savedInstanceState: Bundle?
) {
    val editText: EditText = activity.findViewById(editTextId)
    val clearButton: ImageView = activity.findViewById(contentClearButton)

    init {
        val searchContentEditTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        }

        editText.addTextChangedListener(searchContentEditTextWatcher)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    activity.startTracksSearchingOnQuery()
                }
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

    fun initializeQuery(activity: SearchActivity) {
        App.searchQuery = SearchQuery(App.SEARCH_TRACKS_BASE_URL, ItunesApi::class.java)
        App.searchQuery!!.addObserver(activity)
        App.searchQuery!!.executeTracksQuery(editText.text.toString())
    }

    fun clearQuery() {
        App.searchQuery = null
    }

    fun resumeQuery(activity: SearchActivity): QueryStatus {
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