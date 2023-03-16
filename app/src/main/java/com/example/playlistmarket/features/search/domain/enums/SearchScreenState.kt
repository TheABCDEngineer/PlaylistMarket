package com.example.playlistmarket.features.search.domain.enums

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.example.playlistmarket.R
import com.example.playlistmarket.App

enum class SearchScreenState(
    val title: String? = null,
    val image: Drawable? = null,
    val message: String?= null,
    val functionalButton: FunctionalButtonMode = FunctionalButtonMode.REFRESH,
    val isTitle: Boolean = false,
    val isFeed: Boolean = false,
    val isFunctionalButton: Boolean = false,
    val isProgressBar: Boolean = false
) {
    NEWBORN(),

    HISTORY(
        title= App.appContext.getString(R.string.recent_tracks_list_title),
        functionalButton = FunctionalButtonMode.CLEAR_HISTORY,
        isTitle = true,
        isFeed = true,
        isFunctionalButton = true,
    ),

    QUERY_RESULTS(
        isFeed = true,
    ),

    SEARCHING(
        isProgressBar = true
    ),

    NO_RESULTS(
        image = AppCompatResources.getDrawable(App.appContext, R.drawable.no_any_content),
        message = App.appContext.getString(R.string.search_request_status_text_if_no_results),
    ),

    SOMETHING_WENT_WRONG(
        image = AppCompatResources.getDrawable(App.appContext, R.drawable.warning_icon),
        message = App.appContext.getString(R.string.search_request_status_text_if_server_error),
        isFunctionalButton = true,
    ),

    NO_INTERNET_CONNECTION(
        image = AppCompatResources.getDrawable(App.appContext, R.drawable.no_internet_connection),
        message = App.appContext.getString(R.string.search_request_status_text_if_no_internet),
        isFunctionalButton = true,
    )
}
