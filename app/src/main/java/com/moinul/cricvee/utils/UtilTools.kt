package com.moinul.cricvee.utils

import android.util.Log
import com.moinul.cricvee.R
import java.util.*

private const val TAG = "UtilTools"
class UtilTools {
    companion object{
        class Option(){
            var optionName: String = ""
            var optionImage: Int = 0
            var optionImagePath: String = ""
            var optionLeagueId: Int = 0
            var optionSeasonId: Int = 0
            var optionStageId: Int = 0
            var optionLeagueName:String = ""

            constructor(optionName:String, optionImage:Int) : this() {
                this.optionImage = optionImage
                this.optionName = optionName
            }
            constructor(optionStageId: Int, optionName: String, optionImagePath: String, optionLeagueName: String) : this() {
                this.optionStageId = optionStageId
                this.optionName = optionName
                this.optionImagePath = optionImagePath
                this.optionLeagueName = optionLeagueName
            }

            constructor(optionName: String, optionImagePath:String, optionLeagueId:Int, optionSeasonId:Int): this(){
                this.optionName = optionName
                this.optionImagePath = optionImagePath
                this.optionLeagueId = optionLeagueId
                this.optionSeasonId = optionSeasonId
            }

        }
        var CLICKED_FIXTURE_ID = 0
        val allOptions = listOf<Option>( Option( "ICC Men's Team Rankings", R.drawable.icc_team_ranking),
            Option("ICC Women's Team Rankings", R.drawable.icc_team_ranking)
            )

        var DURATION = getDurationRange()
        var UPCOMING_DURATION = getUpcomingDurationRange()

        fun getDurationRange():String{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val currentDate = "$year-${month+1}-$day"

            calendar.add(Calendar.MONTH, -3) //Max 9 months back

            val modYear = calendar.get(Calendar.YEAR)
            val modMonth = calendar.get(Calendar.MONTH)
            val modDay = calendar.get(Calendar.DAY_OF_MONTH)

            val threeMonthsBack = "$modYear-${modMonth+1}-$modDay"
            Log.d(TAG, "getThreeMonthRange: $currentDate  $threeMonthsBack")
            return "$threeMonthsBack,$currentDate"
        }

        fun getUpcomingDurationRange():String{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val currentDate = "$year-${month+1}-$day"

            calendar.add(Calendar.MONTH, +6) //Max 9 months back

            val modYear = calendar.get(Calendar.YEAR)
            val modMonth = calendar.get(Calendar.MONTH)
            val modDay = calendar.get(Calendar.DAY_OF_MONTH)

            val threeMonthsForward = "$modYear-${modMonth+1}-$modDay"
            Log.d(TAG, "getSixMonthRange: $currentDate  $threeMonthsForward")
            return "$currentDate,$threeMonthsForward"
        }
    }
}