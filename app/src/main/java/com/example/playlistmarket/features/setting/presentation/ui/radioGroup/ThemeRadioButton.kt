package com.example.playlistmarket.features.setting.presentation.ui.radioGroup

import android.widget.RadioButton

class ThemeRadioButton(
    private val button: RadioButton,
    private val group: ThemeRadioGroup
) {
    init {
        group.add(button)
    }

    fun checked() {
        button.isChecked = true
        group.checked(button)
    }
}