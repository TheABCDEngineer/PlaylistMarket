package com.example.playlistmarket.features.medialibrary.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel

class PlaylistAdapter(
    private val playlists: ArrayList<PlaylistRecyclerModel>,
    private val onItemClickedAction: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_recycler_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClickedAction.invoke(playlists[position].id)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun updateItems(items: ArrayList<PlaylistRecyclerModel>) {
        playlists.clear()
        playlists.addAll(items)
    }
}