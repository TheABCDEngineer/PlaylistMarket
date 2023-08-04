package com.example.playlistmarket.features.setting.presentation.viewModel

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmarket.R
import com.example.playlistmarket.App.Companion.appContext
import com.example.playlistmarket.App.Companion.CLICK_DEBOUNCE_DELAY_MILLIS
import com.example.playlistmarket.root.debounce
import com.example.playlistmarket.root.domain.repository.SettingsRepository

class SettingsViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    fun getThemeModeValue(): String {
        return when(
            settingsStorage.getThemeModeValue()
        ) {
            AppCompatDelegate.MODE_NIGHT_NO -> appContext.getString(R.string.light_theme)
            AppCompatDelegate.MODE_NIGHT_YES -> appContext.getString(R.string.dark_theme)
            else -> appContext.getString(R.string.default_theme)
        }
    }

    fun executeShare() {
        val action = debounce<Unit>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope) { _ ->
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                appContext.getString(R.string.settings_share_object))
            shareIntent.type = "text/plain"
            val intent = Intent.createChooser(shareIntent, null)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }
        action.invoke(Unit)
    }

    fun sendMailToSupport() {
        val action = debounce<Unit>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope) { _ ->
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                appContext.getString(R.string.settings_mailto_subject)
            )
            mailIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(appContext.getString(R.string.settings_mailto_addressee))
            )
            mailIntent.putExtra(
                Intent.EXTRA_TEXT,
                appContext.getString(R.string.settings_mailto_letter)
            )
            mailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(mailIntent)
        }
        action.invoke(Unit)
    }

    fun presentUserTerms() {
        val action = debounce<Unit>(CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope) { _ ->
            val webpage: Uri = Uri.parse(appContext.getString(R.string.settings_user_terms_link))
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appContext.startActivity(intent)
        }
        action.invoke(Unit)
    }
}