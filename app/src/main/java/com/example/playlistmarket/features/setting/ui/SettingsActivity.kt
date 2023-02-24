package com.example.playlistmarket.features.setting.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.example.playlistmarket.R
import com.example.playlistmarket.features.setting.presenter.SettingsPresenter
import com.example.playlistmarket.features.setting.presenter.SettingsView
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.creator.ActivityByPresenter

class SettingsActivity : ActivityByPresenter(), SettingsView {
    private val goBackButton: ImageView by lazy { findViewById(R.id.settings_goBack) }
    private val themeSwitcher: Switch by lazy { findViewById(R.id.settings_DarkThemeSwitcher) }
    private val shareButton: TextView by lazy { findViewById(R.id.settings_Share) }
    private val mailButton: TextView by lazy { findViewById(R.id.settings_MailToSupport) }
    private val userTermsButton: TextView by lazy { findViewById(R.id.settings_UserTerms) }
    override val presenter: SettingsPresenter by lazy { Creator.settingsPresenter!! }

    override fun createPresenter() {
        Creator.createSettingsPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        createPresenter()

        goBackButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            presenter.activateDarkTheme(isChecked)
        }

        shareButton.setOnClickListener {
            presenter.executeShare()
        }

        mailButton.setOnClickListener {
            presenter.sendMailToSupport()
        }

        userTermsButton.setOnClickListener {
            presenter.presentUserTerms()
        }
    }
/*
    override fun createController() {
        Creator.createSettingsPresenter(this)
    }

 */

    override fun updateThemeSwitcher(isChecked: Boolean) {
        themeSwitcher.isChecked = isChecked
    }
/*
    override fun onResume() {
        super<AppCompatActivity>.onResume()
        super<ControlledActivity>.onResume()
    }

    override fun onBackPressed() {
        super<ControlledActivity>.onBackPressed()
        super<AppCompatActivity>.onBackPressed()
    }

    override fun onStop() {
        super<ControlledActivity>.onStop()
        super<AppCompatActivity>.onStop()
    }

    override fun onDestroy() {
        super<ControlledActivity>.onDestroy()
        super<AppCompatActivity>.onDestroy()
    }

 */
}