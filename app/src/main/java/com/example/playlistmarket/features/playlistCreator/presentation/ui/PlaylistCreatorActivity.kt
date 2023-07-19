package com.example.playlistmarket.features.playlistCreator.presentation.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ActivityPlaylistCreatorBinding
import com.example.playlistmarket.features.playlistCreator.presentation.EditScreenState
import com.example.playlistmarket.features.playlistCreator.presentation.viewModel.PlaylistCreatorViewModel
import com.example.playlistmarket.root.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistCreatorActivity : AppCompatActivity() {
    private val track: Track? by lazy { intent.getParcelableExtra(App.TRACK_KEY)}
    private lateinit var binding: ActivityPlaylistCreatorBinding
    private val backButton: ImageButton by lazy { binding.backButton }
    private val placeHolder: ImageView by lazy { binding.placeHolder }
    private val artwork: ImageView by lazy { binding.setImage }
    private val titleEditor: EditText by lazy { binding.playlistTitleField }
    private val titleFieldHeader: TextView by lazy { binding.titleFieldHeader }
    private val uniqueTitleWarning: TextView by lazy { binding.uniqueTitleWarning }
    private val descriptionEditor: EditText by lazy { binding.playlistDescriptionField }
    private val descriptionFieldHeader: TextView by lazy { binding.descriptionFieldHeader }
    private val createPlaylistButton: AppCompatButton by lazy { binding.createButton }

    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finishOnBackPressed()
        }
    }

    private val viewModel by viewModel<PlaylistCreatorViewModel> {
        parametersOf(track)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        viewModel.apply {
            observeArtworkImage()
                .observe(this@PlaylistCreatorActivity) {updateArtwork(it)}
            observeTitleFieldState()
                .observe(this@PlaylistCreatorActivity) {updateTitleFieldState(it)}
            observeDescriptionFieldState()
                .observe(this@PlaylistCreatorActivity) {updateDescriptionFieldState(it)}
        }
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        artwork.setOnClickListener {
            viewModel.setArtwork()
        }
        initiateEditText(titleEditor) { charSequence ->
            viewModel.onTitleFieldTextChange(charSequence)
        }
        initiateEditText(descriptionEditor) { charSequence ->
            viewModel.onDescriptionFieldTextChange(charSequence)
        }
        createPlaylistButton.setOnClickListener {
            val playlistTitle = viewModel.createPlayList(this)
            val message = getString(R.string.playlist) + " " + playlistTitle + " " + getString(R.string.has_been_created)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            this.finish()
        }
        viewModel.onUiCreate(this)
    }

    private fun updateArtwork(uri: Uri) {
        placeHolder.isVisible = false
        artwork.setImageURI(uri)
    }

    private fun updateTitleFieldState(state: EditScreenState) {
        titleFieldHeader.isVisible = state.isHeaderVisible
        titleEditor.background = state.backGround
        uniqueTitleWarning.isVisible = state.isWarningMessageVisible
        createPlaylistButton.isEnabled = state.isButtonEnable
    }

    private fun updateDescriptionFieldState(state: EditScreenState) {
        descriptionFieldHeader.isVisible = state.isHeaderVisible
        descriptionEditor.background = state.backGround
    }

    private fun initiateEditText(view: EditText, action: (CharSequence?)-> Unit) {
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                action.invoke(s)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun finishOnBackPressed() {
        if (allowedFinishOnBackPressed()) finish()

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.do_playlist_creation_complete)
        dialog.setMessage(R.string.unsaved_data_will_be_loose)
        dialog.setPositiveButton(R.string.complete){ _, _-> this.finish() }
        dialog.setNegativeButton(R.string.cancel){ _,_-> }
        dialog.show()
    }

    private fun allowedFinishOnBackPressed(): Boolean {
        if (!placeHolder.isVisible) return false
        if (!titleEditor.text.isNullOrEmpty()) return false
        if (!descriptionEditor.text.isNullOrEmpty()) return false
        return true
    }
}