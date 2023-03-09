package com.example.playlistmarket.features.setting.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmarket.R
import com.example.playlistmarket.features.setting.viewModel.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private val goBackButton: ImageView by lazy { findViewById(R.id.settings_goBack) }
    private val themeSwitcher: Switch by lazy { findViewById(R.id.settings_DarkThemeSwitcher) }
    private val shareButton: TextView by lazy { findViewById(R.id.settings_Share) }
    private val mailButton: TextView by lazy { findViewById(R.id.settings_MailToSupport) }
    private val userTermsButton: TextView by lazy { findViewById(R.id.settings_UserTerms) }
    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, SettingsViewModel.getViewModelFactory())[SettingsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        viewModel.darkThemeObserve().observe(this) {
            updateThemeSwitcher(it)
        }

        goBackButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.activateDarkTheme(isChecked)
        }

        shareButton.setOnClickListener {
            viewModel.executeShare()
        }

        mailButton.setOnClickListener {
            viewModel.sendMailToSupport()
        }

        userTermsButton.setOnClickListener {
            viewModel.presentUserTerms()
        }
    }

    private fun updateThemeSwitcher(isChecked: Boolean) {
        themeSwitcher.isChecked = isChecked
    }
}