package com.example.playlistmarket.features.medialibrary.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.FragmentPlaylistPropertiesBinding
import com.example.playlistmarket.features.medialibrary.presentation.ui.widgets.PlaylistInfoWidget
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.PlaylistPropertiesViewModel
import com.example.playlistmarket.features.player.presentation.Player
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.presentation.ui.recyclerView.TrackAdapter
import com.example.playlistmarket.root.showSimpleAlertDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistPropertiesFragment: Fragment() {
    private val viewModel by viewModel<PlaylistPropertiesViewModel>()
    private lateinit var playlistInfoWidget: PlaylistInfoWidget
    private lateinit var binding: FragmentPlaylistPropertiesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistPropertiesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setPlaylist(arguments?.getInt(App.PLAYLIST_KEY))
        playlistInfoWidget = PlaylistInfoWidget(
            binding,
            TrackAdapter(
                ArrayList(),
                { track -> Player.start(track) },
                { track -> onFeedItemLongClicked(track) }
            )
        )

        val menuBottomSheet = BottomSheetBehavior.from(binding.playlistPropertiesBottomSheetMenu)
        val overlay = binding.playlistOverlay

        viewModel.observePlaylistNullException().observe(viewLifecycleOwner) { exception ->
            if (exception) executePlaylistNullExceptionDialog()
        }
        viewModel.observePlaylistProperties().observe(viewLifecycleOwner) { model ->
            playlistInfoWidget.updatePlaylistProperties(model)
            playlistInfoWidget.updatePlaylistInfoCard(model)
        }
        viewModel.observeTracks().observe(viewLifecycleOwner) { tracks ->
            playlistInfoWidget.updateTracksFeed(tracks)
        }
        binding.playlistPropertiesBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.playlistMenu.setOnClickListener {
            overlay.alpha = 0F
            overlay.isVisible = true
            menuBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        menuBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        menuBottomSheet.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.isVisible = false
                    }
                    else -> {}
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = ((slideOffset + 1.0) / 2).toFloat()
            }
        })
    }

    private fun executePlaylistNullExceptionDialog() {
        showSimpleAlertDialog(
            context = this.requireContext(),
            title = getString(R.string.error),
            message = getString(R.string.cant_load_playlist_data),
            positiveButtonTitle = "OK"
        )
        findNavController().popBackStack()
    }

    private fun onFeedItemLongClicked(track: Track) {
        showSimpleAlertDialog(
            context = this.requireContext(),
            title = getString(R.string.delete_a_track),
            message = getString(R.string.warning_delete_a_track),
            positiveButtonTitle = getString(R.string.delete),
            positiveButtonAction = { viewModel.deleteTrackFromPlaylist(track) },
            negativeButtonTitle = getString(R.string.cancel)
        )
    }
}