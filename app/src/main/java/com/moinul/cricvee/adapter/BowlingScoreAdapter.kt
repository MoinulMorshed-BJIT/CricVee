package com.moinul.cricvee.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.R
import com.moinul.cricvee.model.fixturesWithScoreboard.FixtureWithScoreboard
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.bowling_score_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "BowlingScoreAdapter"

class BowlingScoreAdapter(
    val context: Context,
    val viewModel: SportsViewModel,
    val scoreboard: FixtureWithScoreboard,
    scorecardIndex: Int
) : RecyclerView.Adapter<BowlingScoreAdapter.BowlingScoreViewHolder>() {
    private var bowlingList =
        scoreboard.data?.bowling?.filter { it.scoreboard == "S${scorecardIndex}" }

    class BowlingScoreViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BowlingScoreViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.bowling_score_item, parent, false)
        return BowlingScoreViewHolder(root)
    }

    override fun getItemCount(): Int {
        return bowlingList?.size ?: 0
    }

    override fun onBindViewHolder(holder: BowlingScoreViewHolder, position: Int) {
        val bowler = bowlingList?.get(position)
        Log.d(TAG, "onBindViewHolder: $bowler")

        val playerId = bowler?.player_id
        val playerName = holder.itemView.bowler_name
        var playerNameValue = ""
        val overs = holder.itemView.bowler_overs
        val maidens = holder.itemView.bowler_maidens
        val runsConceded = holder.itemView.bowler_runs_conceded
        val wickets = holder.itemView.bowler_wickets
        val economyRate = holder.itemView.bowler_economy_rate

        GlobalScope.launch(Dispatchers.Default) {
            for (player in scoreboard.data?.lineup!!) {
                if (player.id == bowler?.player_id) {
                    playerNameValue = player.fullname.toString()
                    break
                }
            }
            GlobalScope.launch(Dispatchers.Main) {
                playerName.text = playerNameValue
                overs.text = bowler?.overs.toString()
                maidens.text = bowler?.medians.toString()
                runsConceded.text = bowler?.runs.toString()
                wickets.text = bowler?.wickets.toString()
                economyRate.text = bowler?.rate.toString()
            }
        }
    }
}
