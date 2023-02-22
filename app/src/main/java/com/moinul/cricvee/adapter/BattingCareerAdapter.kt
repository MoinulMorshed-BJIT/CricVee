package com.moinul.cricvee.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.R
import com.moinul.cricvee.model.career.CareerData
import com.moinul.cricvee.model.career.CareerX
import com.moinul.cricvee.model.career.LocalBattingCareerLastSeason
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.batting_score_item.view.*
import kotlinx.android.synthetic.main.career_item.view.*
import kotlinx.android.synthetic.main.fragment_player_career.view.*

private const val TAG = "BattingCareerAdapter"
class BattingCareerAdapter (val context: Context, val viewModel: SportsViewModel, val careerData: CareerData, val careerListFromFragment:List<CareerX>)
    : RecyclerView.Adapter<BattingCareerAdapter.BattingCareerViewHolder>() {
    private var careerList = careerListFromFragment





    class BattingCareerViewHolder(private val binding: View): RecyclerView.ViewHolder(binding){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattingCareerViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.career_item, parent, false)
        return BattingCareerViewHolder(root)
    }

    override fun getItemCount(): Int {
        return 14
    }

    override fun onBindViewHolder(holder: BattingCareerViewHolder, position: Int) {
        val parameterTxtV = holder.itemView.career_parameter
        val testPerformanceTxtV = holder.itemView.test_career_value
        val odiPerformanceTxtV = holder.itemView.odi_career_value
        val t20iPerformanceTxtV = holder.itemView.t20i_career_value

        var testCareer:CareerX? = null
        var odiCareer: CareerX? = null
        var t20iCareer:CareerX? = null

        try {
            testCareer = careerList?.filter { it.type=="Test/5day" }?.first()
            odiCareer =   careerList?.filter { it.type=="ODI" }?.first()
            t20iCareer = careerList?.filter { it.type=="T20I" }?.first()
        }catch (e:Exception){
            Log.d(TAG, "onBindViewHolder: $e")
        }

        parameterTxtV.text = Constants.battingParameters[position]
        when(position){
            0->{
                testPerformanceTxtV.text = testCareer?.batting?.matches.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.matches.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.matches.toString()
            }
            1->{
                testPerformanceTxtV.text = testCareer?.batting?.innings.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.innings.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.innings.toString()
            }
            2->{
                testPerformanceTxtV.text = testCareer?.batting?.runs_scored.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.runs_scored.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.runs_scored.toString()
            }
            3->{
                testPerformanceTxtV.text = testCareer?.batting?.not_outs.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.not_outs.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.not_outs.toString()
            }
            4->{
                testPerformanceTxtV.text = testCareer?.batting?.highest_inning_score.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.highest_inning_score.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.highest_inning_score.toString()
            }
            5->{
                var number = testCareer?.batting?.strike_rate
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.batting?.strike_rate
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.batting?.strike_rate
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }
            }
            6->{
                testPerformanceTxtV.text = testCareer?.batting?.balls_faced.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.balls_faced.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.balls_faced.toString()
            }
            7->{
                var number = testCareer?.batting?.average
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.batting?.average
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.batting?.average
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }
            }
            8->{
                testPerformanceTxtV.text = testCareer?.batting?.four_x.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.four_x.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.four_x.toString()
            }
            9->{
                testPerformanceTxtV.text = testCareer?.batting?.six_x.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.six_x.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.six_x.toString()
            }
            10->{
                testPerformanceTxtV.text = testCareer?.batting?.fow_score.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.fow_score.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.fow_score.toString()
            }
            11->{
                var number = testCareer?.batting?.fow_balls
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.batting?.fow_balls
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.batting?.fow_balls
                if(number!=null){
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }
            }
            12->{
                testPerformanceTxtV.text = testCareer?.batting?.hundreds.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.hundreds.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.hundreds.toString()
            }
            13->{
                testPerformanceTxtV.text = testCareer?.batting?.fifties.toString()
                odiPerformanceTxtV.text = odiCareer?.batting?.fifties.toString()
                t20iPerformanceTxtV.text = t20iCareer?.batting?.fifties.toString()
            }
        }

        if(testPerformanceTxtV.text=="null" || testPerformanceTxtV.text==""){testPerformanceTxtV.text="0"}
        if(odiPerformanceTxtV.text=="null" || odiPerformanceTxtV.text==""){odiPerformanceTxtV.text="0"}
        if(t20iPerformanceTxtV.text=="null" || t20iPerformanceTxtV.text==""){t20iPerformanceTxtV.text="0"}

    }
}