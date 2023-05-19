package com.example.playlistmarket.features.setting.presentation.viewModel

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.example.playlistmarket.R
import com.example.playlistmarket.App
import com.example.playlistmarket.base.clickDebounce
import com.example.playlistmarket.base.domain.repository.SettingsRepository

class SettingsViewModel(
    private val settingsStorage: SettingsRepository
) : ViewModel() {

    private var isExecuteShareAllowed = true
    private var isSendMailToSupportAllowed = true
    private var isPresentUserTermsAllowed = true

    fun getThemeModeValue(): String {
        return when(
            settingsStorage.getThemeModeValue()
        ) {
            AppCompatDelegate.MODE_NIGHT_NO -> App.appContext.getString(R.string.light_theme)
            AppCompatDelegate.MODE_NIGHT_YES -> App.appContext.getString(R.string.dark_theme)
            else -> App.appContext.getString(R.string.default_theme)
        }
    }

    fun executeShare() {
        if (!clickDebounce(isExecuteShareAllowed) { isExecuteShareAllowed = it }) return
        val context = App.appContext
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.settings_share_object))
        shareIntent.type = "text/plain"
        val intent = Intent.createChooser(shareIntent, null)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun sendMailToSupport() {
        if (!clickDebounce(isSendMailToSupportAllowed) { isSendMailToSupportAllowed = it }) return
        val context = App.appContext
        val mailIntent = Intent(Intent.ACTION_SENDTO)
        mailIntent.data = Uri.parse("mailto:")
        mailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.settings_mailto_subject)
        )
        mailIntent.putExtra(
            Intent.EXTRA_EMAIL,
            arrayOf(context.getString(R.string.settings_mailto_addressee))
        )
        mailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.settings_mailto_letter))
        mailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(mailIntent)
    }

    fun presentUserTerms() {
        if (!clickDebounce(isPresentUserTermsAllowed) { isPresentUserTermsAllowed = it }) return
        val context = App.appContext
        val webpage: Uri = Uri.parse(context.getString(R.string.settings_user_terms_link))
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}