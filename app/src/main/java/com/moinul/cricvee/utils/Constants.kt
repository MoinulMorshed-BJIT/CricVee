package com.moinul.cricvee.utils

import android.util.Log
import com.moinul.cricvee.R
import java.util.*

private const val TAG = "Constants"
class Constants {
    companion object{
        const val BASE_URL = "https://cricket.sportmonks.com/api/v2.0/"
        const val API_KEY = "bux85Cu7ucRrr3vLYgiW8ZKEGAYGCxSOFUUJnZwWOPTKqhy25pwuIcDMfZUK"
        const val UNAVAILABLE = "Loading... Failed"

        const val MENS = "Men's"
        const val WOMENS = "Women's"
        const val TEST = "TEST"
        const val ODI = "ODI"
        const val T20I = "T20I"

        var DURATION = getDurationRange()

        val allOptions = listOf<Option>( Option( "ICC Team Rankings",
                                            R.drawable.icc_team_ranking) )
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
            Log.d(TAG, "getOneAndHalfYearRange: $currentDate  $threeMonthsBack")
            return "$threeMonthsBack,$currentDate"
        }

        class Option(){
            var optionName: String = ""
            var optionImage: Int = 0

            constructor(optionName:String, optionImage:Int) : this() {
                this.optionImage = optionImage
                this.optionName = optionName
            }
        }
    }
    // continents?api_token={API_TOKEN}
}