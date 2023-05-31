package com.example.playlistmarket.features.search.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.root.domain.model.Track

class SearchTrackAdapter(
    private val trackList: ArrayList<Track>,
    private val onItemClickedAction: (Track) -> Unit
) : RecyclerView.Adapter<SearchTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return SearchTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchTrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onItemClickedAction.invoke(trackList[position])
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    fun updateItems(items: ArrayList<Track>) {
        trackList.clear()
        trackList.addAll(items)
    }
}