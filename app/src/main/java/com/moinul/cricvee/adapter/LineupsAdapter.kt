package com.moinul.cricvee.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.fixturesWithScoreboard.Lineup
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.lineup_visitor_team_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val TAG = "LineupsAdapter"

class LineupsAdapter(
    val context: Context,
    val viewModel: SportsViewModel,
    val listFromFragment: List<Lineup>,
    val lineupIndex: Int
) : RecyclerView.Adapter<LineupsAdapter.LineUpViewHolder>() {
    private var playersList = listFromFragment

    class LineUpViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineUpViewHolder {
        var root: View
        if (lineupIndex == 1) {
            root = LayoutInflater.from(parent.context)
                .inflate(R.layout.lineup_visitor_team_item, parent, false)
        } else {
            root = LayoutInflater.from(parent.context)
                .inflate(R.layout.lineup_local_team_item, parent, false)
        }

        Log.d(TAG, "onCreateViewHolder: $playersList")
        return LineUpViewHolder(root)
    }

    override fun getItemCount(): Int {
        return playersList.size
    }

    override fun onBindViewHolder(holder: LineUpViewHolder, position: Int) {

        val player = playersList[position]
        val playerImageView = holder.itemView.lineup_playerImg
        val playerName = holder.itemView.lineup_playerName
        val playerPosition = holder.itemView.player_position

        var countryNameValue = ""
        var countryData: CountryData?
        var countryFlagPath = ""

        Glide.with(context).load(player.image_path).fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .error(R.drawable.ic_connection_error)
            .into(playerImageView)
        playerName.text = player.fullname
        playerPosition.text = player.position?.name


        GlobalScope.launch(Dispatchers.IO) {
            countryData = player.country_id?.let { viewModel.getCountryById(it) }
            countryNameValue = countryData?.name.toString()

            countryFlagPath = countryData?.image_path.toString()
        }

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.PLAYER_NAME_KEY, playerName.text.toString())
            bundle.putString(Constants.FLAG_IMG_PATH_KEY, countryFlagPath)
            bundle.putString(Constants.PLAYER_IMG_PATH_KEY, player.image_path)
            bundle.putString(Constants.PLAYER_COUNTRY_NAME_KEY, countryNameValue)
            bundle.putInt(Constants.PLAYER_ID_KEY, player.id)

            holder.itemView.findNavController().navigate(R.id.playerCareerFragment, bundle)
        }

    }
}
