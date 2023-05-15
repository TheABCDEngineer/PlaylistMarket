package com.example.playlistmarket.base

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmarket.App

fun setDarkMode(status: Boolean) {
    when (status) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

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
