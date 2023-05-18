package com.example.playlistmarket.features.setting.domain

import android.widget.RadioButton

class ThemeRadioGroup {
    private val items = ArrayList<RadioButton>()

    fun add(newItem: RadioButton) {
        items.add(newItem)
    }

    fun checked(button: RadioButton) {
        for (item in items) {
            item.isChecked = false
        }
        button.isChecked = true
    }
}