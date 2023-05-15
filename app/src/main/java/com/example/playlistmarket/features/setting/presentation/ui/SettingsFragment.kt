package com.example.playlistmarket.features.setting.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.playlistmarket.databinding.FragmentSettingsBinding
import com.example.playlistmarket.features.setting.presentation.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val themeSwitcher: Switch by lazy { binding.settingsDarkThemeSwitcher }
    private val shareButton: TextView by lazy { binding.settingsShare }
    private val mailButton: TextView by lazy { binding.settingsMailToSupport }
    private val userTermsButton: TextView by lazy { binding.settingsUserTerms }
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeSwitcher.isChecked = viewModel.getAppDarkModeValue()

        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.activateDarkTheme(isChecked)
        }

        shareButton.setOnClickListener {
            viewModel.executeShare()
        }

        mailButton.setOnClickListener {
            viewModel.sendMailToSupport()
        }

        userTermsButton.setOnClickListener {
            viewModel.presentUserTerms()
        }
    }
}