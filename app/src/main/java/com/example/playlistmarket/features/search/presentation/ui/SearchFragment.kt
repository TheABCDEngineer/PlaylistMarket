package com.example.playlistmarket.features.search.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmarket.root.hideKeyboard
import com.example.playlistmarket.databinding.FragmentSearchBinding
import com.example.playlistmarket.features.search.domain.enums.SearchScreenState
import com.example.playlistmarket.features.search.presentation.ui.recyclerView.SearchTrackAdapter
import com.example.playlistmarket.features.search.presentation.ui.widgets.ScreenStateWidget
import com.example.playlistmarket.features.search.presentation.ui.widgets.SearchingWidget
import com.example.playlistmarket.features.search.presentation.viewModel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment: Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchingWidget: SearchingWidget by lazy { SearchingWidget(binding) }
    private val screenStateWidget: ScreenStateWidget by lazy { ScreenStateWidget(binding) }
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeScreenState().observe(viewLifecycleOwner) {
            updateScreenState(it)
        }
        viewModel.observeTrackFeedState().observe(viewLifecycleOwner) {
            updateTrackFeed(it)
        }
        searchingWidget.apply {
            onUserRequestTextChange = { value ->
                viewModel.onUserRequestTextChange(value)
            }
            clearSearchingConditions = {
                viewModel.setStartScreen()
            }
            hideSearchingKeyboard = {
                hideKeyboard(this@SearchFragment)
            }
        }
        screenStateWidget.apply {
            onFunctionalButtonClick = { mode ->
                viewModel.onFunctionalButtonPressed(mode)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateHistoryState()
    }

    private fun updateScreenState(state: SearchScreenState) {
        screenStateWidget.setScreenState(state)
    }

    private fun updateTrackFeed(adapter: SearchTrackAdapter) {
        screenStateWidget.setTrackFeed(adapter)
    }
}