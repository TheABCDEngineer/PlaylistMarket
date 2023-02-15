package com.example.playlistmarket.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.example.playlistmarket.R
import com.example.playlistmarket.controller.settings.SettingsController
import com.example.playlistmarket.controller.settings.SettingsView
import com.example.playlistmarket.creator.Creator
import com.example.playlistmarket.ui.ControlledActivity

class SettingsActivity : AppCompatActivity(), SettingsView, ControlledActivity {
    private val goBackButton: ImageView by lazy { findViewById(R.id.settings_goBack) }
    private val themeSwitcher: Switch by lazy { findViewById(R.id.settings_DarkThemeSwitcher) }
    private val shareButton: TextView by lazy { findViewById(R.id.settings_Share) }
    private val mailButton: TextView by lazy { findViewById(R.id.settings_MailToSupport) }
    private val userTermsButton: TextView by lazy { findViewById(R.id.settings_UserTerms) }
    override val controller: SettingsController by lazy { Creator.settingsController!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        createController()

        goBackButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            controller.activateDarkTheme(isChecked)
        }

        shareButton.setOnClickListener {
            controller.executeShare()
        }

        mailButton.setOnClickListener {
            controller.sendMailToSupport()
        }

        userTermsButton.setOnClickListener {
            controller.presentUserTerms()
        }
    }

    override fun createController() {
        Creator.createSettingsController(this)
    }

    override fun updateThemeSwitcher(isChecked: Boolean) {
        themeSwitcher.isChecked = isChecked
    }

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
}