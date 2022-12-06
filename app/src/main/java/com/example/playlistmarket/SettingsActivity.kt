package com.example.playlistmarket

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    private lateinit var goBackButton: ImageView
    private lateinit var themeSwitcher: Switch
    private lateinit var shareButton: TextView
    private lateinit var mailButton: TextView
    private lateinit var userTermsButton: TextView

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var fileName: String
    private lateinit var darkModeKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        initializeVariables()

        setOnClickListenersAtViews()

        themeSwitcher.isChecked = sharedPrefs.getBoolean(
            darkModeKey,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun initializeVariables() {
        goBackButton = findViewById(R.id.settings_goBack)
        themeSwitcher = findViewById(R.id.settings_DarkThemeSwitcher)
        shareButton = findViewById(R.id.settings_Share)
        mailButton = findViewById(R.id.settings_MailToSupport)
        userTermsButton = findViewById(R.id.settings_UserTerms)

        fileName = getString(R.string.app_preference_file_name)
        darkModeKey = getString(R.string.dark_mode_status_key)
        sharedPrefs = getSharedPreferences(fileName, MODE_PRIVATE)
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
            sharedPrefs.edit()
                .putBoolean(darkModeKey, isChecked)
                .apply()
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_object))
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent, null))
        }

        mailButton.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_mailto_subject))
            mailIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.settings_mailto_addressee))
            )
            mailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_mailto_letter))
            startActivity(mailIntent)
        }

        userTermsButton.setOnClickListener {
            val webpage: Uri = Uri.parse(getString(R.string.settings_user_terms_link))
            startActivity(Intent(Intent.ACTION_VIEW, webpage))
        }
    }
}