package com.example.playlistmarket.root.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmarket.R
import com.example.playlistmarket.root.presentation.viewModel.RootViewModel
import com.example.playlistmarket.databinding.AvtivityRootBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {
    private lateinit var binding: AvtivityRootBinding
    private val navigationMenu: BottomNavigationView by lazy { binding.bottomNavigationView }
    private val viewModel by viewModel<RootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AvtivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navigationMenu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.themeFragment, R.id.playlistPropertiesFragment -> {
                    navigationMenu.isVisible= false
                }
                else -> {
                    navigationMenu.isVisible= true
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(
            viewModel.getAppDarkModeValue()
        )
    }
}