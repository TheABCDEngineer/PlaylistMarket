package com.example.playlistmarket.root

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmarket.App

fun clickDebounce(
    isClickAllowed: Boolean,
    changeClicker: (Boolean) -> Unit
): Boolean {
    if (isClickAllowed) {
        changeClicker(false)
        App.mainHandler.postDelayed({ changeClicker(true) }, 1000L)
    }
    return isClickAllowed
}

fun hideKeyboard(fragment: Fragment) {
    val activity = fragment.requireActivity() as AppCompatActivity
    activity.currentFocus?.let { view ->
        val inputMethodManager =
            App.appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
