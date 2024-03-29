package com.example.playlistmarket.features.medialibrary.presentation.ui.childFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.playlistmarket.App.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.databinding.FragmentFavoritesBinding
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.FavoritesViewModel
import com.example.playlistmarket.features.player.presentation.Player
import com.example.playlistmarket.root.presentation.ui.recyclerView.TrackAdapter
import com.example.playlistmarket.root.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel by viewModel<FavoritesViewModel>()
    private lateinit var binding: FragmentFavoritesBinding

    private val onAdapterItemClickedAction: (Track) -> Unit
        get() = debounce(CLICK_DEBOUNCE_DELAY, lifecycleScope) { track: Track ->
            Player.start(track)
        }
    private val adapter = TrackAdapter(ArrayList(),onAdapterItemClickedAction)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mediaLibraryFavoritesList.adapter = adapter

        viewModel.observeFavoritesFeedState().observe(viewLifecycleOwner) {
            updateFavoritesFeed(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiResume()
    }

    private fun updateFavoritesFeed(tracks: ArrayList<Track>) {
        adapter.updateItems(tracks)
        binding.apply {
            mediaLibraryFavoritesList.isVisible = tracks.isNotEmpty()
            mediaLibraryFavoritesStatusText.isVisible = tracks.isEmpty()
            mediaLibraryFavoritesStatusImage.isVisible = tracks.isEmpty()
        }
        if (tracks.isEmpty()) return

        binding.mediaLibraryFavoritesList.adapter!!.notifyDataSetChanged()
    }
}