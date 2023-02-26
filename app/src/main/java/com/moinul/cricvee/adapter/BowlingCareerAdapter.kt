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
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.career_item.view.*

private const val TAG = "BowlingCareerAdapter"

class BowlingCareerAdapter(
    val context: Context,
    val viewModel: SportsViewModel,
    val careerData: CareerData,
    val careerListFromFragment: List<CareerX>
) : RecyclerView.Adapter<BowlingCareerAdapter.BowlingCareerViewHolder>() {
    private var careerList = careerListFromFragment


    class BowlingCareerViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BowlingCareerViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.career_item, parent, false)
        return BowlingCareerViewHolder(root)
    }

    override fun getItemCount(): Int {
        return 15
    }

    override fun onBindViewHolder(holder: BowlingCareerViewHolder, position: Int) {
        val parameterTxtV = holder.itemView.career_parameter
        val testPerformanceTxtV = holder.itemView.test_career_value
        val odiPerformanceTxtV = holder.itemView.odi_career_value
        val t20iPerformanceTxtV = holder.itemView.t20i_career_value

        parameterTxtV.text = Constants.bowlingParameters[position]
        var testCareer: CareerX? = null
        var odiCareer: CareerX? = null
        var t20iCareer: CareerX? = null

        try {
            testCareer = careerList.filter { it.type == Constants.TEST }?.first()
            odiCareer = careerList.filter { it.type == Constants.ODI }?.first()
            t20iCareer = careerList.filter { it.type == Constants.T20I }?.first()
        } catch (e: Exception) {
            Log.d(TAG, "onBindViewHolder: $e")
        }
        when (position) {
            0 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.matches.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.matches.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.matches.toString()
            }
            1 -> {
                var number = testCareer?.bowling?.overs
                if (number != null) {
                    val roundedNumber = Math.floor(number * 10) / 10
                    val formattedNumber = String.format("%.1f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.bowling?.overs
                if (number != null) {
                    val roundedNumber = Math.floor(number * 10) / 10
                    val formattedNumber = String.format("%.1f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.bowling?.overs
                if (number != null) {
                    val roundedNumber = Math.floor(number * 10) / 10
                    val formattedNumber = String.format("%.1f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }
            }
            2 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.innings.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.innings.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.innings.toString()
            }
            3 -> {
                var number = testCareer?.bowling?.average
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.bowling?.average
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.bowling?.average
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }
            }
            4 -> {
                var number = testCareer?.bowling?.econ_rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.bowling?.econ_rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.bowling?.econ_rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }

            }
            5 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.medians.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.medians.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.medians.toString()
            }
            6 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.runs.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.runs.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.runs.toString()
            }
            7 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.wickets.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.wickets.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.wickets.toString()
            }
            8 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.wide.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.wide.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.wide.toString()
            }
            9 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.noball.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.noball.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.noball.toString()
            }
            10 -> {
                var number = testCareer?.bowling?.strike_rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.bowling?.strike_rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.bowling?.strike_rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }

            }
            11 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.four_wickets.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.four_wickets.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.four_wickets.toString()
            }
            12 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.five_wickets.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.five_wickets.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.five_wickets.toString()
            }
            13 -> {
                testPerformanceTxtV.text = testCareer?.bowling?.ten_wickets.toString()
                odiPerformanceTxtV.text = odiCareer?.bowling?.ten_wickets.toString()
                t20iPerformanceTxtV.text = t20iCareer?.bowling?.ten_wickets.toString()
            }
            14 -> {
                var number = testCareer?.bowling?.rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    testPerformanceTxtV.text = formattedNumber
                }
                number = odiCareer?.bowling?.rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    odiPerformanceTxtV.text = formattedNumber
                }
                number = t20iCareer?.bowling?.rate
                if (number != null) {
                    val roundedNumber = Math.ceil(number * 100) / 100
                    val formattedNumber = String.format("%.2f", roundedNumber)
                    t20iPerformanceTxtV.text = formattedNumber
                }

            }

        }

        if (testPerformanceTxtV.text == Constants.NULL_STRING || testPerformanceTxtV.text == Constants.BLANK_STRING) {
            testPerformanceTxtV.text = Constants.STRING_0
        }
        if (odiPerformanceTxtV.text == Constants.NULL_STRING || odiPerformanceTxtV.text == Constants.BLANK_STRING) {
            odiPerformanceTxtV.text = Constants.STRING_0
        }
        if (t20iPerformanceTxtV.text == Constants.NULL_STRING || t20iPerformanceTxtV.text == Constants.BLANK_STRING) {
            t20iPerformanceTxtV.text = Constants.STRING_0
        }
    }
}