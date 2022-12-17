package com.example.playlistmarket

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.player.PlayerActivity
import com.google.gson.Gson

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}

fun startPlayer(track: Track) {
    val context = App.appContext
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra(context.getString(R.string.intent_extra_track_key), Gson().toJson(track))
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun setDarkMode(status: Boolean) {
    when (status) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

fun getSharePreferences(): SharedPreferences {
    return App.appContext.getSharedPreferences(
        App.appContext.getString(R.string.app_preference_file_name),
        Context.MODE_PRIVATE
    )
}