package com.example.playlistmarket.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.*

class SearchTrackAdapter(
    private val searchResultTrackList: List<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(), HistoryObservable {

    private lateinit var searchHistory: HistoryObserver

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(searchResultTrackList[position])
        holder.itemView.setOnClickListener {
            startPlayer(searchResultTrackList[position])
            searchHistory.addTrackToRecentList(searchResultTrackList[position])
        }
    }

    override fun getItemCount(): Int {
        return searchResultTrackList.size
    }

    override fun addObserver(observer: HistoryObserver) {
        searchHistory = observer
    }
}