package com.example.playlistmarket.features.player.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.features.medialibrary.domain.PlaylistRecyclerModel

class PlayerPlaylistAdapter(
    private val playlists: ArrayList<PlaylistRecyclerModel>,
    private val onItemClickedAction: (PlaylistRecyclerModel) -> Unit
) : RecyclerView.Adapter<PlayerPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_recycler_single_item, parent, false)
        return PlayerPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onItemClickedAction.invoke(playlists[position])
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