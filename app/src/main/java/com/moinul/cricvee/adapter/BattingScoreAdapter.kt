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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        var playerNameValue = ""
        val runs = holder.itemView.player1_run
        val balls = holder.itemView.player1_balls
        val fours = holder.itemView.player1_fours
        val sixes = holder.itemView.player1_sixes
        val strikeRate = holder.itemView.player1_strike_rate
        var dismissal = holder.itemView.player1_dismissal
        var dismissalValue = ""

        GlobalScope.launch(Dispatchers.Default) {
            for(player in scoreboard.data?.lineup!!){
                if(player.id == batsman?.player_id){
                    playerNameValue = player.fullname.toString()
                    break;
                }
            }
            if(batsman?.fow_balls == 0.0){
                dismissalValue = " not out"
            }else if(batsman?.catch_stump_player_id==null && batsman?.runout_by_id == null){
                dismissalValue = " lbw b/ b "
                for (player in scoreboard.data?.lineup!!){
                    if(player.id == batsman?.bowling_player_id){
                        dismissalValue += player.fullname
                        break;
                    }
                }
            }else if(batsman?.catch_stump_player_id != null
                        && batsman?.bowling_player_id!=null
                        && batsman?.runout_by_id == null){
                var caught = " c "
                var bowled = " b "
                for (player in scoreboard.data?.lineup!!){
                    if(player.id == batsman?.catch_stump_player_id){
                        caught = caught+player.fullname
                    }
                    if(player.id == batsman?.bowling_player_id){
                        bowled = bowled+player.fullname
                    }

                    if(caught.length > 3 && bowled.length > 3){
                        break;
                    }
                }

                dismissalValue = caught+" "+bowled
            }else if(batsman?.catch_stump_player_id!=null
                        && batsman?.bowling_player_id==null
                        && batsman?.runout_by_id==null){
                for(player in scoreboard.data?.lineup!!){
                    if(player.id == batsman?.catch_stump_player_id){
                        dismissalValue = " run out (${player.fullname})"
                        break;
                    }
                }
            } else if(batsman?.runout_by_id!=null
                        && batsman?.catch_stump_player_id ==null
                        && batsman?.bowling_player_id ==null){
                for(player in scoreboard.data?.lineup!!){
                    if(player.id == batsman?.runout_by_id){
                        dismissalValue = " run out (${player.fullname})"
                        break;
                    }
                }
            }else if(batsman?.runout_by_id!=null
                && batsman?.catch_stump_player_id !=null
                && batsman?.bowling_player_id ==null){
                var runOutThrow = ""
                var runOutStumps = ""
                for(player in scoreboard.data?.lineup!!){

                    if(player.id == batsman?.catch_stump_player_id){
                        runOutStumps = player.fullname.toString()
                    }
                    if(player.id == batsman?.runout_by_id){
                        runOutThrow = player.fullname.toString()
                    }
                    if(runOutThrow.length > 1 && runOutStumps.length > 1){
                        break;
                    }
                }
                dismissalValue = " run out ($runOutStumps/$runOutThrow)"
            }
            GlobalScope.launch(Dispatchers.Main) {
                playerName.text = playerNameValue
                runs.text = batsman?.score.toString()
                balls.text = batsman?.ball.toString()
                fours.text = batsman?.four_x.toString()
                sixes.text = batsman?.six_x.toString()
                strikeRate.text = batsman?.rate.toString()
                dismissal.text = dismissalValue
            }
        }







    }
}
