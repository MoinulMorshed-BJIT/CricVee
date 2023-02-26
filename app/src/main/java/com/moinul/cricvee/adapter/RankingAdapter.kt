package com.moinul.cricvee.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
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
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.player_item.view.*
import kotlinx.android.synthetic.main.rank_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "RankingAdapter"
class RankingAdapter(val context: Context, val viewModel: SportsViewModel, val listFromFragment: List<LocalTeamRanking>)
    : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {
    private var rankList = listFromFragment

    class RankingViewHolder(private val binding: View): RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.rank_item, parent, false)
        return RankingViewHolder(root)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val countryRank = rankList[position]
        val countryPositionTextView = holder.itemView.rankValue
        val countryFlagImageView = holder.itemView.country_flag_img
        val countryNameTextView = holder.itemView.country_name
        val matchesValueTextView = holder.itemView.matchesValue
        val ratingsValueTextView = holder.itemView.ratingValue
        val pointsValueTextView = holder.itemView.pointsValue
        Log.d(TAG, "onBindViewHolder: RankList Size: ${itemCount}")
        if(countryRank.gender=="women"){
            Log.d(TAG, "onBindViewHolder: TeamID: ${countryRank.team_id} CountryID: ${countryRank.country_id}")
        }

        GlobalScope.launch(Dispatchers.Default) {
            val country = countryRank.country_id?.let { viewModel.getCountryById(it) }
//            val country = viewModel.getCountryByName(countryRank.country_id!!)
            Log.d(TAG, "onBindViewHolder: ${countryRank.country_id}")
            Log.d(TAG, "onBindViewHolder: $country")
            var flagImage = country?.image_path
            var countryName = country?.name
            if(country==null){
                flagImage = countryRank.name?.let { viewModel.getTeamByName(it).image_path }
                countryName = countryRank.name
            }
            GlobalScope.launch(Dispatchers.Main) {
                Glide.with(context).load(flagImage).fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .priority(Priority.HIGH)
                    .error(R.drawable.ic_connection_error)
                    .into(countryFlagImageView)
                countryNameTextView.text = countryName
            }
        }
        countryPositionTextView.text = countryRank.position.toString()
        matchesValueTextView.text = "${Constants.MATCHES_COLON} ${countryRank.matches.toString()}"
        ratingsValueTextView.text = countryRank.rating.toString()
        pointsValueTextView.text = countryRank.points.toString()


    }
}