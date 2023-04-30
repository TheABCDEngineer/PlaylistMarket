package com.example.playlistmarket.features.medialibrary.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ActivityMediaLibraryBinding
import com.example.playlistmarket.features.medialibrary.presentation.ui.viewPager2.MediaLibraryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.mediaLibraryGoBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager,lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.media_library_favorites_tab_title)
                1 -> tab.text = getString(R.string.media_library_playlists_tab_title)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}