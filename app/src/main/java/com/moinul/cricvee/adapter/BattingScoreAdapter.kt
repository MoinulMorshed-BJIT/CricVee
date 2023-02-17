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
import kotlinx.android.synthetic.main.batting_score_item.view.*

private const val TAG = "BattingScoreAdapter"
class BattingScoreAdapter (val context: Context, val viewModel: SportsViewModel, val scoreboard: FixtureWithScoreboard, scorecardIndex: Int)
    : RecyclerView.Adapter<BattingScoreAdapter.BattingScoreViewHolder>() {
    private var battingList = scoreboard.data?.batting?.filter { it.scoreboard == "S${scorecardIndex}" }

    class BattingScoreViewHolder(private val binding: View): RecyclerView.ViewHolder(binding){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattingScoreViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.batting_score_item, parent, false)
        return BattingScoreViewHolder(root)
    }

    override fun getItemCount(): Int {
        return battingList?.size ?: 0
    }

    override fun onBindViewHolder(holder: BattingScoreViewHolder, position: Int) {
        val batsman  = battingList?.get(position)
        Log.d(TAG, "onBindViewHolder: $batsman")

        val playerId = batsman?.player_id
        val playerName = holder.itemView.player1_name
        val runs = holder.itemView.player1_run
        val balls = holder.itemView.player1_balls
        val fours = holder.itemView.player1_fours
        val sixes = holder.itemView.player1_sixes
        val strikeRate = holder.itemView.player1_strike_rate

        for(player in scoreboard.data?.lineup!!){
            if(player.id == batsman?.player_id){
                playerName.text = player.fullname
                break;
            }
        }

        runs.text = batsman?.score.toString()
        balls.text = batsman?.ball.toString()
        fours.text = batsman?.four_x.toString()
        sixes.text = batsman?.six_x.toString()
        strikeRate.text = batsman?.rate.toString()



    }
}
