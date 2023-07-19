package com.example.playlistmarket.features.medialibrary.presentation.ui.childFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.playlistmarket.databinding.FragmentPlaylistsBinding
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.PlaylistsViewModel
import com.example.playlistmarket.features.playlistCreator.PlaylistCreator
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    companion object{
        fun  newInstance() = PlaylistsFragment()
    }

    private val viewModel by viewModel<PlaylistsViewModel>()
    private lateinit var binding: FragmentPlaylistsBinding
    private lateinit var newPlaylistButton: Button

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
            PlaylistCreator.start(null)
        }
    }
}
