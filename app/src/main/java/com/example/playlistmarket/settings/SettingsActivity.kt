package com.example.playlistmarket.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.setDarkMode

class SettingsActivity : AppCompatActivity() {
    private lateinit var goBackButton: ImageView
    private lateinit var themeSwitcher: Switch
    private lateinit var shareButton: TextView
    private lateinit var mailButton: TextView
    private lateinit var userTermsButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        initializeVariables()

        setOnClickListenersAtViews()

        themeSwitcher.isChecked = App.sharedPref.getBoolean(
            App.DARK_MODE_STATUS_KEY,
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    private fun initializeVariables() {
        goBackButton = findViewById(R.id.settings_goBack)
        themeSwitcher = findViewById(R.id.settings_DarkThemeSwitcher)
        shareButton = findViewById(R.id.settings_Share)
        mailButton = findViewById(R.id.settings_MailToSupport)
        userTermsButton = findViewById(R.id.settings_UserTerms)
    }

    private fun setOnClickListenersAtViews() {
        goBackButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            setDarkMode(isChecked)
            App.sharedPref.edit()
                .putBoolean(App.DARK_MODE_STATUS_KEY, isChecked)
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