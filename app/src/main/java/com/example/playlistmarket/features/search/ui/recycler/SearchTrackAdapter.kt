package com.example.playlistmarket.features.search.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.creator.observe.Observable
import com.example.playlistmarket.R
import com.example.playlistmarket.creator.enums.PlaylistHandle
import com.example.playlistmarket.creator.observe.Observer
import com.example.playlistmarket.domain.model.Track
import com.example.playlistmarket.creator.startPlayer

class SearchTrackAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(), Observable {

    private lateinit var historyPlaylist: Observer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            startPlayer(trackList[position])
            historyPlaylist.notifyObserver(PlaylistHandle.ADD_TRACK, trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun addObserver(observer: Observer) {
        historyPlaylist = observer
    }
}