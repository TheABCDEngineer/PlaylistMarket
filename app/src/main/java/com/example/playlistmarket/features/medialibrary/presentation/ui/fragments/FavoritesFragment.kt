package com.example.playlistmarket.features.medialibrary.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.playlistmarket.databinding.FragmentFavoritesBinding
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.FavoritesViewModel
import com.example.playlistmarket.features.search.presentation.ui.recyclerView.SearchTrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritesFragment()
    }
    private val viewModel by viewModel<FavoritesViewModel>()
    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        viewModel.observeTrackFeedState().observe(viewLifecycleOwner) {
            updateFavoritesFeed(it)
        }

        return binding.root
    }

    private fun updateFavoritesFeed(trackAdapter: SearchTrackAdapter?) {
        if (trackAdapter == null) return

        val listIsEmpty = trackAdapter.itemCount == 0
        binding.apply {
            mediaLibraryFavoritesList.isVisible = !listIsEmpty
            mediaLibraryFavoritesStatusText.isVisible = listIsEmpty
            mediaLibraryFavoritesStatusImage.isVisible = listIsEmpty
        }
        if (listIsEmpty) return

        binding.mediaLibraryFavoritesList.apply {
            adapter = trackAdapter
            adapter!!.notifyDataSetChanged()
        }

    }
}