package com.example.playlistmarket.features.search.viewModel.enums

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.example.playlistmarket.R
import com.example.playlistmarket.App

enum class SearchScreenState(
    val title: String?,
    val image: Drawable?,
    val message: String?,
    val functionalButton: FunctionalButtonMode,
    val isTitle: Boolean,
    val isFeed: Boolean,
    val isFunctionalButton: Boolean,
    val isProgressBar: Boolean
) {
    NEWBORN(
        null,
        null,
        null,
        FunctionalButtonMode.REFRESH,
        false,
        false,
        false,
        false
    ),
    HISTORY(
        App.appContext.getString(R.string.recent_tracks_list_title),
        null,
        null,
        FunctionalButtonMode.CLEAR_HISTORY,
        true,
        true,
        true,
        false
    ),
    QUERY_RESULTS(
        null,
        null,
        null,
        FunctionalButtonMode.REFRESH,
        false,
        true,
        false,
        false
    ),
    SEARCHING(
        null,
        null,
        null,
        FunctionalButtonMode.REFRESH,
        false,
        false,
        false,
        true
    ),
    NO_RESULTS(
        null,
        AppCompatResources.getDrawable(App.appContext, R.drawable.no_any_content),
        App.appContext.getString(R.string.search_request_status_text_if_no_results),
        FunctionalButtonMode.REFRESH,
        false,
        false,
        false,
        false
    ),
    SOMETHING_WENT_WRONG(
        null,
        AppCompatResources.getDrawable(App.appContext, R.drawable.warning_icon),
        App.appContext.getString(R.string.search_request_status_text_if_server_error),
        FunctionalButtonMode.REFRESH,
        false,
        false,
        true,
        false
    ),
    NO_INTERNET_CONNECTION(
        null,
        AppCompatResources.getDrawable(App.appContext, R.drawable.no_internet_connection),
        App.appContext.getString(R.string.search_request_status_text_if_no_internet),
        FunctionalButtonMode.REFRESH,
        false,
        false,
        true,
        false
    )
}
