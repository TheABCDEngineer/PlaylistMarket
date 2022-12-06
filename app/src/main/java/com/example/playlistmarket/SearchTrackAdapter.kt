package com.example.playlistmarket

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class SearchTrackAdapter(
    private val searchResultTrackList: List<Track>
) : RecyclerView.Adapter<SearchTrackViewHolder>(), Observable {

    private lateinit var searchHistory: Observer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(searchResultTrackList[position])
        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Запустился плеер",
                Toast.LENGTH_SHORT
            ).show()

            searchHistory.addTrackToRecentList(searchResultTrackList[position])
        }
    }

    override fun getItemCount(): Int {
        return searchResultTrackList.size
    }

    override fun addObserver(observer: Observer) {
        searchHistory = observer
    }
}