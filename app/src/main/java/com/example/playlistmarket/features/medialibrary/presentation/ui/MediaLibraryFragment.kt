package com.example.playlistmarket.features.medialibrary.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentMediaLibraryBinding
import com.example.playlistmarket.features.medialibrary.presentation.ui.viewPager2.MediaLibraryViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryFragment: Fragment() {
    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(childFragmentManager,lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.media_library_favorites_tab_title)
                1 -> tab.text = getString(R.string.media_library_playlists_tab_title)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        tabMediator.detach()
        super.onDestroyView()
    }
}