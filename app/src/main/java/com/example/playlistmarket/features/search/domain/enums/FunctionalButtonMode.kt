package com.example.playlistmarket.features.search.domain.enums

import com.example.playlistmarket.R
import com.example.playlistmarket.App

enum class FunctionalButtonMode(
    val title: String,
    val layoutHeight: Int,
    val layoutWeight: Float
) {
    REFRESH(
        App.appContext.getString(R.string.search_refresh_button_title),
        -2,//ViewGroup.LayoutParams.WRAP_CONTENT
        0F
    ),
    CLEAR_HISTORY(
        App.appContext.getString(R.string.clear_history_button),
        0,
        1F
    )
}