package com.example.playlistmarket.features.playlistCreator.presentation

enum class EditScreenState(
    val isHeaderVisible: Boolean,
    val isWarningMessageVisible: Boolean = false,
    val isButtonEnable: Boolean = false
) {
    ACTIVE_FIELD(
        isHeaderVisible = true,
        isButtonEnable = true
    ),
    INACTIVE_FIELD(
        isHeaderVisible = false,
        ),
    UNIQUE_TITLE(
        isHeaderVisible = true,
        isWarningMessageVisible = true
    )
}