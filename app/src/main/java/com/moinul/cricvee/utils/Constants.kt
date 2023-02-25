package com.moinul.cricvee.utils

import android.util.Log
import com.moinul.cricvee.R
import java.util.*

private const val TAG = "Constants"
class Constants {
    companion object{
        const val BASE_URL = "https://cricket.sportmonks.com/api/v2.0/"
        const val API_KEY = "JGD4ZcPgkJq1waNCkx8CydFwlwuMdQa3dpWWIhJ0pWW9sLKY7gVb8usITjNe"

//        const val API_KEY = "bux85Cu7ucRrr3vLYgiW8ZKEGAYGCxSOFUUJnZwWOPTKqhy25pwuIcDMfZUK"
//        const val API_KEY = "dk2eWyJAQmtLKQxD1R2Dq3tLzvfkWNFtqDGAzcfZlNxD9PTHLsTvJzgkVE1V"
//        const val API_KEY = "4757MuW3D2lWSaXCde1gifYrPAMldHsXsepoiyFmSBc55azhEJhLvLgpxRhW"
//        const val API_KEY = "Uxxy4dILiSaSNRETXC7KpydBwxxBf05Ru5XU1H2r2vn9Ocu2kUbHN2ARJtxM"
//        const val API_KEY = "PcvoBre62XxgTdtfAyaNjCb2ZTynH4QGKStFTQaZnmpSjDx53qhsJDgWtX3U"
        const val UNAVAILABLE = "Loading... Failed"

        const val MENS = "Men's"
        const val WOMENS = "Women's"
        const val TEST = "TEST"
        const val ODI = "ODI"
        const val T20I = "T20I"

        val battingParameters = listOf("Matches", "Innings", "Runs",
            "Not Outs","Highest","S/R","Balls","Average","Fours","Sixes",
            "FoW Score","FoW Balls","100s","50s")
        val bowlingParameters = listOf("Matches","Overs","Innings","Average",
            "ECO","Maidens","Runs","Wickets", "Wides","No balls","S/R","4Ws","5Ws","10Ws",
            "Rate")




    }
    // continents?api_token={API_TOKEN}
}