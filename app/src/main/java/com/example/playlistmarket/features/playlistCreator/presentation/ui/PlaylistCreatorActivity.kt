package com.example.playlistmarket.features.playlistCreator.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.databinding.ActivityPlaylistCreatorBinding
import com.example.playlistmarket.features.playlistCreator.presentation.EditScreenState
import com.example.playlistmarket.features.playlistCreator.presentation.viewModel.PlaylistCreatorViewModel
import com.example.playlistmarket.root.domain.model.Track
import com.example.playlistmarket.root.showSimpleAlertDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.properties.Delegates

class PlaylistCreatorActivity : AppCompatActivity() {
    private var track: Track? = null
    private var playlistId by Delegates.notNull<Int>()
    private lateinit var binding: ActivityPlaylistCreatorBinding
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
        parametersOf(track, playlistId)
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) viewModel.setArtwork()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        track = if (Build.VERSION.SDK_INT < 33)
            intent.getParcelableExtra(App.TRACK_KEY)
        else
            intent.getParcelableExtra(App.TRACK_KEY, Track::class.java)

        playlistId = intent.getIntExtra(App.PLAYLIST_KEY,-1)
        if (playlistId != -1) {
            createPlaylistButton.text = getString(R.string.to_save)
            binding.playlistCreatorInterfaceHeader.text = getString(R.string.to_modify)
        }

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        viewModel.apply {
            observeArtworkImage()
                .observe(this@PlaylistCreatorActivity) {updateArtwork(it)}
            observeTitleFieldState()
                .observe(this@PlaylistCreatorActivity) {updateTitleFieldState(it)}
            observeDescriptionFieldState()
                .observe(this@PlaylistCreatorActivity) {updateDescriptionFieldState(it)}
            observePlaylistInfo()
                .observe(this@PlaylistCreatorActivity) {updateEditFieldText(it.title, it.description)}
        }
        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        artwork.setOnClickListener {
            if (Build.VERSION.SDK_INT < 33) {
                viewModel.setArtwork()
                return@setOnClickListener
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED)
                viewModel.setArtwork()
            else
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
        initiateEditText(titleEditor) { charSequence ->
            viewModel.onTitleFieldTextChange(charSequence)
        }
        initiateEditText(descriptionEditor) { charSequence ->
            viewModel.onDescriptionFieldTextChange(charSequence)
        }
        createPlaylistButton.setOnClickListener {
            val playlistTitle = viewModel.savePlayList()
            val message = if (track == null)
                getString(R.string.playlist) + " " + playlistTitle + " " + getString(R.string.has_been_created) else
                App.appContext.getString(R.string.added_to_playlist) + " \"" + playlistTitle + "\""
            if (playlistTitle != null) Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            this.finish()
        }
        viewModel.onUiCreate(this)
    }

    private fun updateArtwork(uri: Uri?) {
        placeHolder.isVisible = false
        if (uri != null) {
            artwork.setImageURI(uri)
            artwork.scaleType = ImageView.ScaleType.CENTER_CROP
            return
        }
        artwork.setImageDrawable(
            AppCompatResources.getDrawable(this, R.drawable.default_album_image)
        )
        artwork.scaleType = ImageView.ScaleType.FIT_CENTER
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

    private fun updateEditFieldText(title: String = "", description: String = "") {
        if (title != "") titleEditor.setText(title)
        if (description != "") descriptionEditor.setText(description)
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

        showSimpleAlertDialog(
            context = this,
            title = getString(R.string.do_playlist_creation_complete),
            message = getString(R.string.unsaved_data_will_be_loose),
            positiveButtonTitle = getString(R.string.complete),
            positiveButtonAction = { this.finish() },
            negativeButtonTitle = getString(R.string.cancel)
        )
    }

    private fun allowedFinishOnBackPressed(): Boolean {
        if (playlistId != -1) return true
        if (!placeHolder.isVisible) return false
        if (!titleEditor.text.isNullOrEmpty()) return false
        if (!descriptionEditor.text.isNullOrEmpty()) return false
        return true
    }
}