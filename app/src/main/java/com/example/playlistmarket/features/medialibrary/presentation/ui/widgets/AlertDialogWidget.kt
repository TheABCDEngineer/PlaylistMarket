package com.example.playlistmarket.features.medialibrary.presentation.ui.widgets

import android.content.Context
import com.example.playlistmarket.R
import com.example.playlistmarket.root.showSimpleAlertDialog

class AlertDialogWidget(
    val context: Context
) {
    fun showPlaylistNullExceptionDialog() {
        showSimpleAlertDialog(
            context = context,
            title = context.getString(R.string.error),
            message = context.getString(R.string.cant_load_playlist_data),
            positiveButtonTitle = context.getString(R.string.ok)
        )
    }

    fun showOnFeedItemLongClicked(action: () -> Unit) {
        showSimpleAlertDialog(
            context = context,
            title = context.getString(R.string.delete_a_track),
            message = context.getString(R.string.warning_delete_a_track),
            positiveButtonTitle = context.getString(R.string.yes),
            positiveButtonAction = action,
            negativeButtonTitle = context.getString(R.string.no)
        )
    }

    fun showOnDeletePlaylistClicked(action: () -> Unit) {
        showSimpleAlertDialog(
            context = context,
            title = context.getString(R.string.delete_a_playlist),
            message = context.getString(R.string.warning_delete_a_playlist),
            positiveButtonTitle = context.getString(R.string.yes),
            positiveButtonAction = action,
            negativeButtonTitle = context.getString(R.string.no)
        )
    }

    fun showOnImpossibleShare() {
        showSimpleAlertDialog(
            context = context,
            message = context.getString(R.string.impossible_to_share_playlist),
            negativeButtonTitle = context.getString(R.string.ok)
        )
    }
}