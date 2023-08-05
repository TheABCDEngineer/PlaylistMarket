package com.example.playlistmarket.features.medialibrary.presentation.ui.childFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlaylistsBinding
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel
import com.example.playlistmarket.features.medialibrary.presentation.ui.recyclerView.PlaylistAdapter
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.PlaylistsViewModel
import com.example.playlistmarket.root.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    companion object{
        fun  newInstance() = PlaylistsFragment()
    }
    private val viewModel by viewModel<PlaylistsViewModel>()
    private var binding: FragmentPlaylistsBinding? = null
    private var newPlaylistButton: Button? = null
    private var placeholderImage: ImageView? = null
    private var placeholderMessage: TextView? = null
    private var playlistsRecycler: RecyclerView? = null

    private val adapter = PlaylistAdapter(
        playlists = ArrayList(),
        onItemClickedAction = debounce(App.CLICK_DEBOUNCE_DELAY_MILLIS, lifecycleScope) { playlistId: Int ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playlistPropertiesFragment,
                Bundle().apply { putInt(App.PLAYLIST_KEY,playlistId) }
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newPlaylistButton = binding?.newPlaylistButton
        newPlaylistButton?.setOnClickListener{
            viewModel.onNewPlaylistButtonClicked()
        }
        placeholderImage = binding?.mediaLibraryPlaylistsStatusImage
        placeholderMessage = binding?.mediaLibraryPlaylistsStatusText

        playlistsRecycler = binding?.mediaLibraryPlaylistsTable
        playlistsRecycler?.layoutManager = GridLayoutManager(requireContext(),2)
        playlistsRecycler?.adapter = adapter

        viewModel.observeFeedState().observe(viewLifecycleOwner) {
            updateFeed(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiResume()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun updateFeed(items: ArrayList<PlaylistRecyclerModel>) {
        adapter.updateItems(items)
        playlistsRecycler?.adapter?.notifyDataSetChanged()
        placeholderImage?.isVisible = items.isEmpty()
        placeholderMessage?.isVisible = items.isEmpty()
    }
}
