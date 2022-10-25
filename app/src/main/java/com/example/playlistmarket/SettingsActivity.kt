package com.example.playlistmarket

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.hide()

        val goBackButton = findViewById<ImageView>(R.id.settings_goBack)
        val themeSwitch = findViewById<Switch>(R.id.settings_DarkThemeSwitch)
        val shareButton = findViewById<TextView>(R.id.settings_Share)
        val mailButton = findViewById<TextView>(R.id.settings_MailToSupport)
        val userTermsButton = findViewById<TextView>(R.id.settings_UserTerms)

        goBackButton.setOnClickListener {
            onBackPressed()
        }

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        
        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_object))
            shareIntent.type = "text/plain"
            startActivity(Intent.createChooser(shareIntent,null))
        }

        mailButton.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_mailto_subject))
            mailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.settings_mailto_addressee)))
            mailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_mailto_letter))
            startActivity(mailIntent)
        }

        userTermsButton.setOnClickListener {
            val webpage: Uri = Uri.parse(getString(R.string.settings_user_terms_link))
            startActivity(Intent(Intent.ACTION_VIEW, webpage))
        }
    }
}