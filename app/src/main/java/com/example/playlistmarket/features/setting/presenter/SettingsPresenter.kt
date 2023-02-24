package com.example.playlistmarket.features.setting.presenter

import android.content.Intent
import android.net.Uri
import com.example.playlistmarket.R
import com.example.playlistmarket.creator.Presenter
import com.example.playlistmarket.App
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.clickDebounce
import com.example.playlistmarket.creator.setDarkMode
import com.example.playlistmarket.data.repository.storage.SettingsStorageRepository

class SettingsPresenter(
    var view: SettingsView,
    private val settingsStorage: SettingsStorageRepository
) : Presenter() {

    private var isActivateDarkThemeAllowed = true
    private var isExecuteShareAllowed = true
    private var isSendMailToSupportAllowed = true
    private var isPresentUserTermsAllowed = true

    init {
        view.updateThemeSwitcher(settingsStorage.getDarkModeStatusValue())
    }

    fun activateDarkTheme(isDarkTheme: Boolean) {
        if (!clickDebounce(isActivateDarkThemeAllowed) { isActivateDarkThemeAllowed = it }) return
        setDarkMode(isDarkTheme)
        settingsStorage.putDarkModeStatusValue(isDarkTheme)
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

    override fun implementPresenterDestroyingMethod(): () -> Unit = {
        Creator.settingsPresenter = null
    }
}