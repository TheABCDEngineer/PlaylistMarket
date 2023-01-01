package com.example.playlistmarket.search.widgets

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmarket.App
import com.example.playlistmarket.Track
import com.example.playlistmarket.TrackListHandler
import com.example.playlistmarket.search.SearchActivity
import com.example.playlistmarket.search.recycler.SearchTrackAdapter

class RecyclerViewWidget(
    activity: Activity,
    recyclerId: Int,
    recyclerLayoutId: Int,
    recyclerTitle: Int
) {
    val recycler: RecyclerView = activity.findViewById(recyclerId)
    private val recyclerLayout: LinearLayout = activity.findViewById(recyclerLayoutId)
    val title: TextView = activity.findViewById(recyclerTitle)

    private val queryTrackList = ArrayList<Track>()
    val searchHistory = TrackListHandler(App.sharedPref, App.RECENT_TRACKS_LIST_KEY, 10)

    val queryAdapter = SearchTrackAdapter(queryTrackList)
    private val historyAdapter = SearchTrackAdapter(searchHistory.items)

    init {
        queryAdapter.addObserver(searchHistory)
        historyAdapter.addObserver(searchHistory)
        title.visibility = View.GONE
    }

    fun prepareToShowQueryPlaceholder() {
        recyclerLayout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        recyclerLayout.layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = 0F
        }
        title.visibility = View.GONE
        recycler.visibility = View.GONE
    }

    fun prepareToShowHistory() {
        recyclerLayout.layoutParams.height = 0
        recyclerLayout.layoutParams.apply {
            (this as LinearLayout.LayoutParams).weight = 1F
        }
        recycler.adapter = historyAdapter
        recycler.adapter!!.notifyDataSetChanged()
        recycler.visibility = View.VISIBLE
        title.visibility = View.VISIBLE
    }

    fun clearHistory() {
        recycler.visibility = View.GONE
        title.visibility = View.GONE
        searchHistory.clear()
    }

    fun prepareToShowQueryResults(trackList: ArrayList<Track>) {
        queryTrackList.clear()
        queryTrackList.addAll(trackList)
        recycler.adapter = queryAdapter
        recycler.adapter!!.notifyDataSetChanged()
        recycler.visibility = View.VISIBLE
    }

    fun onActivityResume() {
        if (recycler.adapter == historyAdapter) {
            recycler.adapter!!.notifyDataSetChanged()
        }
    }
}