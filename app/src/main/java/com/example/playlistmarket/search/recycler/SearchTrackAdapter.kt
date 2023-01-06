package com.example.playlistmarket.search.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.App
import com.example.playlistmarket.Observable
import com.example.playlistmarket.Observer
import com.example.playlistmarket.R
import com.example.playlistmarket.medialibrary.Track
import com.example.playlistmarket.startPlayer

class SearchTrackAdapter(
    private val searchResultTrackList: List<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(), Observable {

    private lateinit var listOwner: Observer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(searchResultTrackList[position])
        holder.itemView.setOnClickListener {
            startPlayer(searchResultTrackList[position])
            listOwner.notifyObserver(App.TRACK_ADD,searchResultTrackList[position])
        }
    }

    override fun getItemCount(): Int {
        return searchResultTrackList.size
    }

    override fun addObserver(observer: Observer) {
        listOwner = observer
    }
}