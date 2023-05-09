package com.example.playlistmarket.base.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmarket.R
import com.example.playlistmarket.base.presentation.viewModel.RootViewModel
import com.example.playlistmarket.base.setDarkMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {
    private val navigationMenu: BottomNavigationView by lazy { findViewById(R.id.bottomNavigationView) }
    private val viewModel by viewModel<RootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_root)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
        navigationMenu.setupWithNavController(navHostFragment.navController)

        val isDarkMode = viewModel.getAppDarkModeValue()
        setDarkMode(isDarkMode)
    }
}