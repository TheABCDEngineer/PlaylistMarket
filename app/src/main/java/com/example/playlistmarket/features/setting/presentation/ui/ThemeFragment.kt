package com.example.playlistmarket.features.setting.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmarket.databinding.FragmentThemeSettingsBinding
import com.example.playlistmarket.features.setting.domain.ThemeRadioButton
import com.example.playlistmarket.features.setting.domain.ThemeRadioGroup
import com.example.playlistmarket.features.setting.presentation.viewModel.ThemeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ThemeFragment: Fragment() {
    private lateinit var binding: FragmentThemeSettingsBinding
    private val viewModel by viewModel<ThemeViewModel>()
    private val radioGroup = ThemeRadioGroup()
    private lateinit var defaultButton: ThemeRadioButton
    private lateinit var lightButton: ThemeRadioButton
    private lateinit var darkButton: ThemeRadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeSettingsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRadioGroup()
        binding.defaultThemeRadioButton.setOnClickListener {
            defaultButton.checked()
            viewModel.onUserModeSelected(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        binding.lightThemeRadioButton.setOnClickListener {
            lightButton.checked()
            viewModel.onUserModeSelected(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.darkThemeRadioButton.setOnClickListener {
            darkButton.checked()
            viewModel.onUserModeSelected(AppCompatDelegate.MODE_NIGHT_YES)
        }
        updateThemeRadioGroupState(
            viewModel.getThemeMode()
        )
    }

    private fun initRadioGroup() {
        defaultButton = ThemeRadioButton(binding.defaultThemeRadioButton, radioGroup)
        lightButton = ThemeRadioButton(binding.lightThemeRadioButton, radioGroup)
        darkButton = ThemeRadioButton(binding.darkThemeRadioButton, radioGroup)
    }

    private fun updateThemeRadioGroupState(themeMode: Int) {
        when (themeMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> defaultButton.checked()
            AppCompatDelegate.MODE_NIGHT_NO -> lightButton.checked()
            AppCompatDelegate.MODE_NIGHT_YES -> darkButton.checked()
        }
    }
}