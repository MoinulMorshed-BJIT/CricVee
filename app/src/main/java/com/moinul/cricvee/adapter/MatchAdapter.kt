package com.moinul.cricvee.adapter

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
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.fixtures.FixtureRunData
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.match_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.log

class MatchAdapter(val context: Context, val viewModel: SportsViewModel, val listFromFragment: List<FixtureData>)
    : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    private val matchList = listFromFragment


    class MatchViewHolder(private val binding: View): RecyclerView.ViewHolder(binding){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        return MatchViewHolder(root)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matchList[position]
        var team1Data:TeamData?
        var team2Data: TeamData?
        var fixtureRunData: FixtureRunData?
        var team1runAndWicketsData: String = ""
        var team1overData: String = ""
        var team2runAndWicketsData: String = ""
        var team2overData: String = ""


        val team1imageView = holder.itemView.team1_img
        val team2imageView = holder.itemView.team2_img
        val team1name = holder.itemView.team1_name
        val team2name = holder.itemView.team2_name
        val noteResult = holder.itemView.match_note_result
        val team1score = holder.itemView.team1_score
        val team1overs = holder.itemView.team1_overs
        val team2score = holder.itemView.team2_score
        val team2overs = holder.itemView.team2_overs

        GlobalScope.launch(Dispatchers.Default) {
            team1Data = match.visitorteam_id?.let { viewModel.readTeamById(it) }!!
            team2Data = match.localteam_id?.let { viewModel.readTeamById(it) }!!

            //Log.d("TAG", "onBindViewHolder: ${match.id}")
            fixtureRunData = viewModel.fetchRunsByFixtureId(match.id)
            Log.d("TAG", "onBindViewHolder: ${fixtureRunData?.runs?.get(0)?.team_id}")
            if(fixtureRunData?.runs?.get(0)?.team_id == match.visitorteam_id){
                team1runAndWicketsData = fixtureRunData?.runs?.get(0)?.score.toString()+" / "+fixtureRunData?.runs?.get(0)?.wickets.toString()
                team1overData = "( ${fixtureRunData?.runs?.get(0)?.overs.toString()} )"

                team2runAndWicketsData = fixtureRunData?.runs?.get(1)?.score.toString()+" / "+fixtureRunData?.runs?.get(1)?.wickets.toString()
                team2overData = "( ${fixtureRunData?.runs?.get(1)?.overs.toString()} )"
            }else{
                team2runAndWicketsData = fixtureRunData?.runs?.get(0)?.score.toString()+" / "+fixtureRunData?.runs?.get(0)?.wickets.toString()
                team2overData = "( ${fixtureRunData?.runs?.get(0)?.overs.toString()} )"


                team1runAndWicketsData = fixtureRunData?.runs?.get(1)?.score.toString()+"/"+fixtureRunData?.runs?.get(1)?.wickets.toString()
                team1overData = "( ${fixtureRunData?.runs?.get(1)?.overs.toString()} )"
            }

            GlobalScope.launch(Dispatchers.Main) {

                Glide.with(context).load(team1Data?.image_path).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .priority(Priority.HIGH)
                    .error(R.drawable.ic_connection_error)
                    .into(team1imageView)

                Glide.with(context).load(team2Data?.image_path).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .priority(Priority.HIGH)
                    .error(R.drawable.ic_connection_error)
                    .into(team2imageView)

                team1name.text = team1Data?.name.toString()
                team1score.text = team1runAndWicketsData
                team1overs.text = team1overData


                team2name.text = team2Data?.name.toString()
                team2score.text = team2runAndWicketsData
                team2overs.text = team2overData


            }
                /*name1 = team1Data?.name.toString()
                name2 = team2Data?.name.toString()
                image1path = team1Data?.image_path.toString()
                image2path = team2Data?.image_path.toString()*/

        }
        /*Glide.with(context).load(image1path).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .into(team1imageView)

        Glide.with(context).load(image2path).centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .into(team2imageView)

        team1name.text = name1
        team2name.text = name2*/
        noteResult.text = match.note








//        team1imageView.setImageResource(R.drawable.ic_home_black_24dp)
//        team2imageView.setImageResource(R.drawable.ic_dashboard_black_24dp)






    }

}