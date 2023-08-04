package com.example.playlistmarket.features.setting.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmarket.databinding.FragmentThemeSettingsBinding
import com.example.playlistmarket.features.setting.presentation.ui.radioGroup.ThemeRadioButton
import com.example.playlistmarket.features.setting.presentation.ui.radioGroup.ThemeRadioGroup
import com.example.playlistmarket.features.setting.presentation.viewModel.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThemeFragment: Fragment() {
    private var binding: FragmentThemeSettingsBinding? = null
    private val viewModel by viewModel<ThemeViewModel>()
    private val radioGroup = ThemeRadioGroup()
    private var defaultButton: ThemeRadioButton? = null
    private var lightButton: ThemeRadioButton? = null
    private var darkButton: ThemeRadioButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThemeSettingsBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (binding == null) return
        initRadioGroup()
        binding!!.defaultThemeRadioButton.setOnClickListener {
            defaultButton?.checked()
            viewModel.onUserModeSelected(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        binding!!.lightThemeRadioButton.setOnClickListener {
            lightButton?.checked()
            viewModel.onUserModeSelected(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding!!.darkThemeRadioButton.setOnClickListener {
            darkButton?.checked()
            viewModel.onUserModeSelected(AppCompatDelegate.MODE_NIGHT_YES)
        }
        updateThemeRadioGroupState(
            viewModel.getThemeMode()
        )
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun initRadioGroup() {
        if (binding == null) return
        defaultButton = ThemeRadioButton(binding!!.defaultThemeRadioButton, radioGroup)
        lightButton = ThemeRadioButton(binding!!.lightThemeRadioButton, radioGroup)
        darkButton = ThemeRadioButton(binding!!.darkThemeRadioButton, radioGroup)
    }

    private fun updateThemeRadioGroupState(themeMode: Int) {
        when (themeMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> defaultButton?.checked()
            AppCompatDelegate.MODE_NIGHT_NO -> lightButton?.checked()
            AppCompatDelegate.MODE_NIGHT_YES -> darkButton?.checked()
        }
    }
}