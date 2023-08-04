package com.example.playlistmarket.features.medialibrary.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmarket.App
import com.example.playlistmarket.databinding.FragmentPlaylistPropertiesBinding
import com.example.playlistmarket.features.medialibrary.presentation.ui.widgets.AlertDialogWidget
import com.example.playlistmarket.features.medialibrary.presentation.ui.widgets.PlaylistInfoWidget
import com.example.playlistmarket.features.medialibrary.presentation.viewModel.PlaylistPropertiesViewModel
import com.example.playlistmarket.features.player.presentation.Player
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.presentation.ui.recyclerView.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistPropertiesFragment: Fragment() {
    private val viewModel by viewModel<PlaylistPropertiesViewModel> {
        parametersOf(arguments?.getInt(App.PLAYLIST_KEY))
    }
    private var binding: FragmentPlaylistPropertiesBinding? = null
    private var playlistInfoWidget: PlaylistInfoWidget? = null
    private lateinit var alertDialogWidget: AlertDialogWidget

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistPropertiesBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertDialogWidget = AlertDialogWidget(this.requireContext())
        if (binding == null) return

        playlistInfoWidget = PlaylistInfoWidget(
            binding!!,
            TrackAdapter(
                trackList = ArrayList(),
                artworkResolution = 60,
                onItemClickedAction = { track -> Player.start(track) },
                onItemLongClickedAction = { track -> onFeedItemLongClicked(track) }
            )
        )
        val menuBottomSheet = BottomSheetBehavior.from(binding!!.playlistPropertiesBottomSheetMenu)
        val overlay = binding!!.playlistOverlay

        setDataObservers()
        setOnClickListeners(overlay, menuBottomSheet)
        setBottomSheetBehavior(overlay, menuBottomSheet)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onUiResume()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun setDataObservers() {
        with(viewModel) {
            observePlaylistLoading().observe(viewLifecycleOwner) { isLoaded ->
                if (!isLoaded) executePlaylistLoadingErrorDialog()
            }
            observePlaylistProperties().observe(viewLifecycleOwner) { model ->
                playlistInfoWidget?.updatePlaylistProperties(model)
                playlistInfoWidget?.updatePlaylistInfoCard(model)
            }
            observeTracks().observe(viewLifecycleOwner) { tracks ->
                playlistInfoWidget?.updateTracksFeed(tracks)
            }
        }
    }

    private fun setOnClickListeners(overlay: View, menuBottomSheet:BottomSheetBehavior<LinearLayout>) {
        if (binding == null) return
        val shareButtonClickListener = View.OnClickListener {
            menuBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            if (!viewModel.sharePlaylist()) onImpossibleShare()
        }
        with(binding!!) {
            playlistPropertiesBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            playlistMenu.setOnClickListener {
                overlay.alpha = 0F
                overlay.isVisible = true
                menuBottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            playlistShare.setOnClickListener(shareButtonClickListener)
            playlistPropertiesShare.setOnClickListener(shareButtonClickListener)
            playlistPropertiesModify.setOnClickListener {
                menuBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                viewModel.modifyPlaylist()
            }
            playlistPropertiesDelete.setOnClickListener {
                onDeletePlaylistClicked()
            }
        }
    }

    private fun setBottomSheetBehavior(overlay: View, menuBottomSheet:BottomSheetBehavior<LinearLayout>) {
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

    private fun executePlaylistLoadingErrorDialog() {
        alertDialogWidget.showOnPlaylistLoadingError()
        findNavController().popBackStack()
    }

    private fun onFeedItemLongClicked(track: Track) {
        alertDialogWidget.showOnFeedItemLongClicked {
            viewModel.deleteTrackFromPlaylist(track)
        }
    }

    private fun onDeletePlaylistClicked() {
        alertDialogWidget.showOnDeletePlaylistClicked {
            viewModel.deletePlaylist()
            findNavController().popBackStack()
        }
    }

    private fun onImpossibleShare() {
        alertDialogWidget.showOnImpossibleShare()
    }
}