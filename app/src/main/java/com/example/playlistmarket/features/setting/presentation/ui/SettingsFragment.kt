package com.example.playlistmarket.features.setting.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentSettingsBinding
import com.example.playlistmarket.features.setting.presentation.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
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

        binding.themeValue.text = viewModel.getThemeModeValue()

        binding.themeChanger.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_themeFragment)
        }

        binding.settingsShare.setOnClickListener {
            viewModel.executeShare()
        }

        binding.settingsMailToSupport.setOnClickListener {
            viewModel.sendMailToSupport()
        }

        binding.settingsUserTerms.setOnClickListener {
            viewModel.presentUserTerms()
        }
    }
}