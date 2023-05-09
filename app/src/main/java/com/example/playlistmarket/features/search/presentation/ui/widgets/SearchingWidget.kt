package com.example.playlistmarket.features.search.presentation.ui.widgets

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import com.example.playlistmarket.R
import com.example.playlistmarket.App
import com.example.playlistmarket.databinding.FragmentSearchBinding

class SearchingWidget(
    binding: FragmentSearchBinding
) {
    private val editText: EditText = binding.searchEditText
    private val clearButton: ImageView = binding.searchClearButton
    lateinit var onUserRequestTextChange: (String) -> Unit
    lateinit var clearSearchingConditions: () -> Unit
    lateinit var hideSearchingKeyboard: () -> Unit

    init {
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

    private fun getRequestTextValue(): String {
        return editText.text.toString()
    }
}