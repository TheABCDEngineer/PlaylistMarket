package com.example.playlistmarket

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_CONTENT_EDIT_TEXT = "searchContentEditText"
    }

    var searchContentEditTextValue = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SEARCH_CONTENT_EDIT_TEXT,
            searchContentEditTextValue
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchContentEditTextValue = savedInstanceState.getString(SEARCH_CONTENT_EDIT_TEXT,"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

        val searchContentEditText = findViewById<EditText>(R.id.search_EditText)
        val searchContentClearButton = findViewById<ImageView>(R.id.search_ClearButton)
        val goBackButton = findViewById<ImageView>(R.id.search_goBack)

        searchContentEditText.setText(searchContentEditTextValue)

        goBackButton.setOnClickListener {
            onBackPressed()
        }

        searchContentClearButton.setOnClickListener {
            searchContentEditText.setText("")
            searchContentClearButton.visibility = View.GONE

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
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
}