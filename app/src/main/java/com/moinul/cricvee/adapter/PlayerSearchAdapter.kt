package com.moinul.cricvee.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.currentPlayers.SquadPlayerData
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.player_item.view.*
import java.util.*

class PlayerSearchAdapter(val context: Context, val viewModel: SportsViewModel, val listFromFragment: List<Squad>)
    : RecyclerView.Adapter<PlayerSearchAdapter.PlayerSearchViewHolder>() {
    private var playersList = listFromFragment

    class PlayerSearchViewHolder(private val binding: View): RecyclerView.ViewHolder(binding){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerSearchViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return PlayerSearchViewHolder(root)
    }

    override fun getItemCount(): Int {
        return playersList.size
    }

    override fun onBindViewHolder(holder: PlayerSearchViewHolder, position: Int) {
        val player = playersList[position]
        val playerImageView = holder.itemView.player_img
        val playerName = holder.itemView.player_name

        Glide.with(context).load(player?.image_path).fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .error(R.drawable.ic_connection_error)
            .into(playerImageView)
        playerName.text = player.fullname

    }

    fun performSearch(text: String) {
        Log.d("Search Test", "performSearch: HERE!")
        val searchResults = ArrayList<Squad>()
        for (player in listFromFragment) {
            //Log.d("List e ki?", "$newsList")
            if (player.fullname?.lowercase(Locale.ROOT)
                    ?.contains(text.lowercase(Locale.ROOT)) == true
            ) {
                searchResults.add(player)
            }
        }
        showResults(searchResults)



    }

    @SuppressLint("NotifyDataSetChanged")
    fun showResults(searchResults: List<Squad>) {
        playersList = searchResults
        notifyDataSetChanged()
    }
}