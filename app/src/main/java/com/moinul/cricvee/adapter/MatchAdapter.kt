package com.moinul.cricvee.adapter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.fixtures.FixtureRunData
import com.moinul.cricvee.model.fixtures.FixtureWithRun
import com.moinul.cricvee.model.league.LeagueData
import com.moinul.cricvee.model.season.SeasonData
import com.moinul.cricvee.model.stage.StageData
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.match_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "MatchAdapter"

class MatchAdapter(
    val context: Context,
    val viewModel: SportsViewModel,
    val listFromFragment: List<FixtureData>,
    private val lifecycleOwner: LifecycleOwner,
    private val inFixtureFragment: Boolean
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    private val matchList = listFromFragment


    class MatchViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        return MatchViewHolder(root)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matchList[position]
        var team1Data: TeamData?
        var team2Data: TeamData?
        var fixtureRunDataResponseResult: Result<FixtureWithRun>
        var fixtureRunData: FixtureRunData?
        var team1runAndWicketsData = Constants.BLANK_STRING
        var team1overData = Constants.BLANK_STRING
        var team2runAndWicketsData = Constants.BLANK_STRING
        var team2overData = Constants.BLANK_STRING
        var matchInfoTitleData = Constants.BLANK_STRING
        var stageData: StageData?
        var leagueData: LeagueData?
        var seasonData: SeasonData?

        val team1imageView = holder.itemView.team1_imgV
        val team2imageView = holder.itemView.team2_imgV
        val team1name = holder.itemView.team1_name
        val team2name = holder.itemView.team2_name
        val noteResult = holder.itemView.match_note_result
        val team1score = holder.itemView.team1_score
        val team1overs = holder.itemView.team1_overs
        val team2score = holder.itemView.team2_score
        val team2overs = holder.itemView.team2_overs
        val round = holder.itemView.round_txtV
        val leagueImage = holder.itemView.leagueImgV
        val stageLeagueTitle = holder.itemView.stage_league_title
        val countdownTimer = holder.itemView.timer_txtV



        if (match.status != Constants.NOT_STARTED) {
            noteResult.text = match.note
        } else {
            noteResult.text = Constants.YET_TO_START
        }


        if (!inFixtureFragment) {
            viewModel.getCountdownList().observe(lifecycleOwner) { countdownList ->
                if (countdownList.isNotEmpty()) {
                    val countdown = countdownList[position]
                    val countdownText = formatCountdownText(countdown)
                    Log.d(TAG, "onBindViewHolder: Countdown: $countdownText")
                    countdownTimer.text = countdownText

                    if (match.status != Constants.NOT_STARTED) {
                        val formatter =
                            SimpleDateFormat(Constants.DATE_FORMAT_STRING, Locale.getDefault())
                        formatter.timeZone = TimeZone.getTimeZone(Constants.UTC_CODE)
                        val date = formatter.parse(match.starting_at)
                        var dateString = date.toString()
                        var timeZoneSliceIndex = dateString.indexOf(Constants.GMT_CODE)
                        var yearString =
                            dateString.substring(timeZoneSliceIndex + 10, dateString.length)
                        dateString = dateString.substring(0, timeZoneSliceIndex)
                        countdownTimer.text = dateString + yearString

                    }
                }
            }

        } else {
            val formatter = SimpleDateFormat(Constants.DATE_FORMAT_STRING, Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone(Constants.UTC_CODE)
            val date = formatter.parse(match.starting_at)
            var dateString = date.toString()
            var timeZoneSliceIndex = dateString.indexOf(Constants.GMT_CODE)
            var yearString = dateString.substring(timeZoneSliceIndex + 10, dateString.length)
            dateString = dateString.substring(0, timeZoneSliceIndex)
            countdownTimer.text = dateString + yearString
        }


        GlobalScope.launch(Dispatchers.Default) {
            try {
                team1Data = match.visitorteam_id?.let { viewModel.readTeamById(it) }!!
                team2Data = match.localteam_id?.let { viewModel.readTeamById(it) }!!
                stageData = match.stage_id?.let { viewModel.readStageById(it) }
                leagueData = match.league_id?.let { viewModel.readLeagueById(it) }

            } catch (e: Exception) {
                team1Data = null
                team2Data = null
                stageData = null
                leagueData = null
                Log.d(TAG, "Catch onBindViewHolder: $e")
            }
            fixtureRunDataResponseResult = viewModel.fetchRunsByFixtureId(match.id)

            if (fixtureRunDataResponseResult.isSuccess) {
                fixtureRunData = fixtureRunDataResponseResult.getOrNull()?.data
                if (fixtureRunData?.runs?.isNotEmpty() == true) {
                    Log.d("TAG", "onBindViewHolder: Parent = $fixtureRunData")
                    Log.d("TAG", "onBindViewHolder: RUNS LIST = ${fixtureRunData?.runs}")
                    if (fixtureRunData?.runs?.first()?.team_id == match.visitorteam_id) {
                        team1runAndWicketsData =
                            Constants.TOTAL_COLON + fixtureRunData?.runs?.first()?.score.toString() + Constants.SLASH + fixtureRunData?.runs?.first()?.wickets.toString()
                        team1overData = "${Constants.OVERS_COLON} ${fixtureRunData?.runs?.first()?.overs.toString()}"

                        if (fixtureRunData?.runs?.size == 2) {
                            team2runAndWicketsData =
                                Constants.TOTAL_COLON + fixtureRunData?.runs?.last()?.score.toString() + Constants.SLASH + fixtureRunData?.runs?.last()?.wickets.toString()
                            team2overData =
                                "${Constants.OVERS_COLON} ${fixtureRunData?.runs?.last()?.overs.toString()}"
                        }
                    } else if (fixtureRunData?.runs?.first()?.team_id == match.localteam_id) {
                        team2runAndWicketsData =
                            Constants.TOTAL_COLON + fixtureRunData?.runs?.first()?.score.toString() + Constants.SLASH + fixtureRunData?.runs?.first()?.wickets.toString()
                        team2overData = "${Constants.OVERS_COLON} ${fixtureRunData?.runs?.first()?.overs.toString()}"

                        if (fixtureRunData?.runs?.size == 2) {
                            team1runAndWicketsData =
                                Constants.TOTAL_COLON + fixtureRunData?.runs?.last()?.score.toString() + Constants.SLASH + fixtureRunData?.runs?.last()?.wickets.toString()
                            team1overData =
                                "${Constants.OVERS_COLON} ${fixtureRunData?.runs?.last()?.overs.toString()}"
                        }
                    }
                } 
            } else {
                team1runAndWicketsData = Constants.BLANK_STRING
                team2runAndWicketsData = Constants.BLANK_STRING
            }

            GlobalScope.launch(Dispatchers.Main) {

                try {
                    Glide.with(context).load(team1Data?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(team1imageView)

                    Glide.with(context).load(team2Data?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(team2imageView)
                    Glide.with(context).load(leagueData?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(leagueImage)

                    team1name.text = team1Data?.code.toString()
                    team1score.text = team1runAndWicketsData
                    team1overs.text = team1overData

                    team2name.text = team2Data?.code.toString()
                    team2score.text = team2runAndWicketsData
                    team2overs.text = team2overData
                    stageLeagueTitle.text =
                        "${leagueData?.name.toString()} | ${stageData?.name.toString()}"
                    round.text = match.round

                    if (countdownTimer.text == Constants.END_COUNTDOWN) {
                        noteResult.text = Constants.MATCH_STARTED
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onBindViewHolder: $e")
                }
            }
        }
        holder.itemView.setOnClickListener {
            UtilTools.CLICKED_FIXTURE_ID = match.id
            holder.itemView.findNavController().navigate(R.id.matchDetailsFragment)
        }
    }

    private fun formatCountdownText(countdown: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(countdown)
        val hours = TimeUnit.MILLISECONDS.toHours(countdown) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(countdown) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(countdown) % 60

        if (days < 0 || hours < 0 || minutes < 0 || seconds < 0) {
            return Constants.END_COUNTDOWN
        }

        return String.format(Constants.COUNTDOWN_OUTPUT_FORMAT, days, hours, minutes, seconds)
    }


}