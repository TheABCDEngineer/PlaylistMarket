package com.example.playlistmarket.features.search.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.base.observe.Observable
import com.example.playlistmarket.R
import com.example.playlistmarket.base.domain.model.enums.PlaylistHandle
import com.example.playlistmarket.base.observe.Observer
import com.example.playlistmarket.base.domain.model.Track
import com.example.playlistmarket.base.startPlayer

class SearchTrackAdapter(
    private val trackList: List<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(), Observable {

    private var historyPlaylist: Observer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            startPlayer(trackList[position])

            if (historyPlaylist != null) {
                historyPlaylist!!.notifyObserver(PlaylistHandle.ADD_TRACK, trackList[position])
            }

        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun addObserver(observer: Observer) {
        historyPlaylist = observer
    }
}