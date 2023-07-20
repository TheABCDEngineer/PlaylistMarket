package com.example.playlistmarket.root.data.files

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmarket.App
import com.example.playlistmarket.R
import com.example.playlistmarket.root.domain.repository.PlaylistArtworksRepository
import java.io.File
import java.io.FileOutputStream

class PlaylistArtworksRepositoryImplFile: PlaylistArtworksRepository {
    private val filePath = File(
        App.appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        App.appContext.getString(R.string.app_name)
    )

    init {
        if (!filePath.exists()) filePath.mkdirs()
    }

    override suspend fun saveArtwork(uri: Uri, fileName: String) {
        val file = File(filePath, "$fileName.jpg")
        BitmapFactory
            .decodeStream(
                App.appContext.contentResolver.openInputStream(uri)
            )
            .compress(
                Bitmap.CompressFormat.JPEG,
                30,
                FileOutputStream(file)
            )
    }

    override suspend fun loadArtwork(fileName: String): Uri? {
        val file = File(filePath, "$fileName.jpg")
        if (file.exists()) return file.toUri()
        return null
    }
}