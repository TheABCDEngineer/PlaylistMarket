package com.example.playlistmarket.controller.search.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.creator.interactors.Observable
import com.example.playlistmarket.R
import com.example.playlistmarket.creator.enums.PlaylistHandle
import com.example.playlistmarket.creator.interactors.Observer
import com.example.playlistmarket.domain.models.Track
import com.example.playlistmarket.creator.startPlayer

class SearchTrackAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(), Observable {

    private lateinit var listOwner: Observer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            startPlayer(trackList[position])
            listOwner.notifyObserver(PlaylistHandle.ADD_TRACK, trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun addObserver(observer: Observer) {
        listOwner = observer
    }
}