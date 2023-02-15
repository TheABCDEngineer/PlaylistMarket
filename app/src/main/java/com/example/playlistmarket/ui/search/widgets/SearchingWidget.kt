package com.example.playlistmarket.ui.search.widgets

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.playlistmarket.R
import com.example.playlistmarket.creator.App
import com.example.playlistmarket.ui.search.SearchActivity

class SearchingWidget(
    activity: SearchActivity
) {
    private val editText: EditText = activity.findViewById(R.id.search_EditText)
    private val clearButton: ImageView = activity.findViewById(R.id.search_ClearButton)
    lateinit var onUserRequestTextChange: (String) -> Unit
    lateinit var clearSearchingConditions: () -> Unit
    lateinit var hideSearchingKeyboard: () -> Unit

    fun initializeSearchingSettings(initialText: String) {
        editText.setText(initialText)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                if (s.isNullOrEmpty()) return
                onUserRequestTextChange.invoke(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onUserRequestTextChange.invoke(getRequestTextValue())
                hideSearchingKeyboard.invoke()
            }
            false
        }

        editText.setOnFocusChangeListener { _, hasFocus ->
            editText.hint =
                if (hasFocus && editText.text.isEmpty()) App.appContext.getString(R.string.search) else ""
        }

        clearButton.setOnClickListener {
            editText.setText("")
            clearButton.isVisible = false
            clearSearchingConditions.invoke()
        }
    }

    fun getRequestTextValue(): String {
        return editText.text.toString()
    }
}