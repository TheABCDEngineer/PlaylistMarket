package com.example.playlistmarket.search.query

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.example.playlistmarket.App
import com.example.playlistmarket.R

enum class ResponseHandle(
    val image: Drawable?,
    val message: String,
    val isRefreshButton: Boolean = false
) {
    SEARCHING(
        AppCompatResources.getDrawable(App.appContext, R.drawable.please_wait_icon),
        App.appContext.getString(R.string.search_status_please_wait)
    ),
    NO_RESULTS(
        AppCompatResources.getDrawable(App.appContext, R.drawable.no_any_content),
        App.appContext.getString(R.string.search_request_status_text_if_no_results),
    ),
    SOMETHING_WENT_WRONG(
        AppCompatResources.getDrawable(App.appContext, R.drawable.warning_icon),
        App.appContext.getString(R.string.search_request_status_text_if_server_error),
        true
    ),
    NO_INTERNET_CONNECTION(
        AppCompatResources.getDrawable(App.appContext, R.drawable.no_internet_connection),
        App.appContext.getString(R.string.search_request_status_text_if_no_internet),
        true
    )

}