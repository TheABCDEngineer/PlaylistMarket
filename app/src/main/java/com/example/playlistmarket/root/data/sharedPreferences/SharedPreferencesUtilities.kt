package com.example.playlistmarket.root.data.sharedPreferences

import android.content.SharedPreferences
import com.google.gson.Gson

inline fun <reified T> loadListFromFileOnKey(
    file: SharedPreferences,
    key: String,
    clazz: Class<Array<T>>
): Array<T> {
    val json: String = file.getString(key, null) ?: return emptyArray()
    return Gson().fromJson(json, clazz)
}

fun <T> loadFromFileOnKey(
    file: SharedPreferences,
    key: String,
    clazz: Class<T>
): T? {
    val json: String = file.getString(key, null) ?: return null
    return Gson().fromJson(json, clazz)
}

fun <T> saveToFileOnKey(
    file: SharedPreferences,
    key: String,
    list: ArrayList<T>
) {
    val json = Gson().toJson(list)
    file.edit()
        .putString(key, json)
        .apply()
}

fun <T> saveToFileOnKey(
    file: SharedPreferences,
    key: String,
    value: T
) {
    val json = Gson().toJson(value)
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