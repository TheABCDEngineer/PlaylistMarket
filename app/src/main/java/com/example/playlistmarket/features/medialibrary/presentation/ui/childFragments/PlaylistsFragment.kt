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
    private lateinit var binding: FragmentPlaylistsBinding
    private lateinit var newPlaylistButton: Button
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderMessage: TextView
    private lateinit var playlistsRecycler: RecyclerView

    private val onAdapterItemClickedAction: (Int) -> Unit
        get() = debounce(App.CLICK_DEBOUNCE_DELAY, lifecycleScope) { playlistId: Int ->
            //viewModel.onPlaylistChoose(playlistId)
//            val bundle = Bundle()
//            bundle.putInt(App.PLAYLIST_KEY,playlistId)
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playlistPropertiesFragment,
                Bundle().apply { putInt(App.PLAYLIST_KEY,playlistId) }
            )
            //NavHostFragment.findNavController(requireActivity()).navigate(R.id.action_mediaLibraryFragment_to_playlistPropertiesFragment)
//            val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment
//            val navController = navHostFragment.navController
//            Log.i("qwert","enter")
//            navController.navigate(R.id.action_mediaLibraryFragment_to_playlistPropertiesFragment)
        }
    private val adapter = PlaylistAdapter(ArrayList(),onAdapterItemClickedAction)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newPlaylistButton = binding.newPlaylistButton
        newPlaylistButton.setOnClickListener{
            viewModel.onNewPlaylistButtonClicked()
        }
        placeholderImage = binding.mediaLibraryPlaylistsStatusImage
        placeholderMessage = binding.mediaLibraryPlaylistsStatusText

        playlistsRecycler = binding.mediaLibraryPlaylistsTable
        playlistsRecycler.layoutManager = GridLayoutManager(requireContext(),2)
        playlistsRecycler.adapter = adapter

        viewModel.observeFeedState().observe(viewLifecycleOwner) {
            updateFeed(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiResume()
    }

    private fun updateFeed(items: ArrayList<PlaylistRecyclerModel>) {
        adapter.updateItems(items)
        placeholderImage.isVisible = items.isEmpty()
        placeholderMessage.isVisible = items.isEmpty()
        playlistsRecycler.adapter!!.notifyDataSetChanged()
    }
}
