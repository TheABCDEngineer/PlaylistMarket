package com.example.playlistmarket.root.presentation.ui.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.R
import com.example.playlistmarket.root.domain.model.Track

class TrackAdapter(
    private val trackList: ArrayList<Track>,
    private val onItemClickedAction: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
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