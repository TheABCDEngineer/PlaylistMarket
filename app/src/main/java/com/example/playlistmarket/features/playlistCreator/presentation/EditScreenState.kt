package com.example.playlistmarket.features.playlistCreator.presentation

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.example.playlistmarket.App
import com.example.playlistmarket.R

enum class EditScreenState(
    val isHeaderVisible: Boolean,
    val backGround: Drawable?,
    val isWarningMessageVisible: Boolean = false,
    val isButtonEnable: Boolean = false
) {
    ACTIVE_FIELD(
        isHeaderVisible = true,
        backGround = AppCompatResources.getDrawable(App.appContext, R.drawable.playlist_creator_text_input),
        isButtonEnable = true
    ),
    INACTIVE_FIELD(
        isHeaderVisible = false,
        backGround = AppCompatResources.getDrawable(App.appContext, R.drawable.playlist_creator_edit_text_empty)
    ),
    UNIQUE_TITLE(
        isHeaderVisible = true,
        backGround = AppCompatResources.getDrawable(App.appContext, R.drawable.playlist_creator_text_input),
        isWarningMessageVisible = true
    )
}