package com.example.playlistmarket

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.player.PlayerActivity
import com.google.gson.Gson

fun startPlayer(track: Track?) {
    if (track == null) return
    val context = App.appContext
    val intent = Intent(context, PlayerActivity::class.java)
    intent.putExtra(App.TRACK_KEY, track)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun setDarkMode(status: Boolean) {
    when (status) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
/*
inline fun <reified T> loadListFromFileOnKey(
    file: SharedPreferences,
    key: String
): Array<T> {
    val json: String = file.getString(key, null) ?: return emptyArray()
    return Gson().fromJson(json, Array<T>::class.java)
}
 */
// ЗНАЮ, ЧТО ТУПОБ ВЕДЬ СЛЕДУЮЩИЕ ДВА МЕТОДА ПО СУТИ ОТЛИЧАЮТСЯ ТОЛЬКО ТИПАМИ,
// НО МОИХ НАЧАЛЬНЫХ НАВЫКОВ НЕ ХВАТАЕТ, ЧТО БЫ ДОПЕДРИТЬ, КАК ИСПОЛЬЗОВАТЬ ДЖЕНЕРИКИ В ЭТОМ СЛУЧАЕ
// NEED HELP ОЧЕНЬ
fun loadTrackListFromFileOnKey(
    file: SharedPreferences,
    key: String
): Array<Track> {
    val json: String = file.getString(key, null) ?: return emptyArray()
    return Gson().fromJson(json, Array<Track>::class.java)
}

fun loadStringListFromFileOnKey(
    file: SharedPreferences,
    key: String
): Array<String> {
    val json: String = file.getString(key, null) ?: return emptyArray()
    return Gson().fromJson(json, Array<String>::class.java)
}

fun <T> saveListToFileOnKey(
    file: SharedPreferences,
    key: String,
    list: ArrayList<T>
) {
    val json = Gson().toJson(list)
    file.edit()
        .putString(key, json)
        .apply()
}

fun deleteFromFileOnKey(
    file: SharedPreferences,
    key: String
) {
    file.edit()
        .remove(key)
        .apply()
}