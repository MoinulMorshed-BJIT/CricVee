package com.moinul.cricvee.utils

private const val TAG = "Constants"

class Constants {
    companion object {
        const val BASE_URL = "https://cricket.sportmonks.com/api/v2.0/"
        const val API_KEY = "dk2eWyJAQmtLKQxD1R2Dq3tLzvfkWNFtqDGAzcfZlNxD9PTHLsTvJzgkVE1V"
        const val MENS = "Men's"
        const val WOMENS = "Women's"
        const val WOMEN_ATTRIBUTE_VALUE = "women"
        const val TEST = "Test/5day"
        const val ODI = "ODI"
        const val T20I = "T20I"
        const val NULL_STRING = "null"
        const val STRING_0 = "0"
        const val BLANK_STRING = ""
        const val NOT_OUT =  " not out"
        const val BOWLED_LBW = " lbw b/ b "
        const val CAUGHT = " c "
        const val BOWLED = " b "
        const val RUN_OUT = " run out "
        const val LEAGUE_ID = "leagueId"
        const val SEASON_ID = "seasonId"
        const val PLAYER_ID_KEY = "playerID"
        const val STAGE_ID_KEY = "stageId"
        const val LEAGUE_IMG_PATH_KEY = "leagueImagePath"
        const val LEAGUE_NAME_KEY = "leagueName"
        const val PLAYER_NAME_KEY = "playerName"
        const val FLAG_IMG_PATH_KEY = "flagImagePath"
        const val PLAYER_IMG_PATH_KEY = "playerImagePath"
        const val PLAYER_COUNTRY_NAME_KEY = "playerCountryName"
        const val NOT_STARTED = "NS"
        const val YET_TO_START = "Yet to start"
        const val DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
        const val UTC_CODE = "UTC"
        const val GMT_CODE = "GMT"
        const val TOTAL_COLON = "Total: "
        const val OVERS_COLON = "Overs:"
        const val SLASH = "/"
        const val END_COUNTDOWN = "Countdown ended"
        const val MATCH_STARTED = "Match started."
        const val COUNTDOWN_OUTPUT_FORMAT = "⏳ %02dD:%02dH:%02dM:%02dS"
        const val MATCHES_COLON = "Matches:"
        const val DATABASE_NAME = "sports_database"
        const val LOCAL_TEAM_TEXT_VIEW_VALUE_KEY = "localTeamTextViewValue"
        const val VISITOR_TEAM_TEXT_VIEW_VALUE_KEY = "visitorTeamTextViewValue"
        const val MATCH_INFO = "MATCH INFO"
        const val SCORECARD = "SCORECARD"
        const val LINEUP = "LINEUP"
        const val WON_THE_TOSS = "won the toss."
        const val MATCH_ROUND_VALUE_KEY = "matchRoundValue"
        const val STAGE_VALUE_KEY = "stageValue"
        const val TIME_VALUE_KEY = "timeValue"
        const val VENUE_VALUE_KEY = "venueValue"
        const val FIRST_UMPIRE_VALUE_KEY = "firstUmpireValue"
        const val SECOND_UMPIRE_VALUE_KEY = "secondUmpireValue"
        const val TV_UMPIRE_VALUE_KEY = "tvUmpireValue"
        const val REFEREE_VALUE_KEY = "refereeValue"
        const val TOSS_WON_VALUE_KEY = "tossWonValue"
        const val APP_NOTIFICATION_CHANNEL_ID = "cricvee_notification_channel"
        const val APP_NOTIFICATION_CHANNEL_NAME = "CricVee Match Notification"
        const val APP_NOTIFICATION_CHANNEL_CONTENT = "A match is about to start in a few minutes!"
        const val INNINGS = " Innings"

        val battingParameters = listOf(
            "Matches", "Innings", "Runs",
            "Not Outs", "Highest", "S/R", "Balls", "Average", "Fours", "Sixes",
            "FoW Score", "FoW Balls", "100s", "50s"
        )
        val bowlingParameters = listOf(
            "Matches", "Overs", "Innings", "Average",
            "ECO", "Maidens", "Runs", "Wickets", "Wides", "No balls", "S/R", "4Ws", "5Ws", "10Ws",
            "Rate"
        )
    }
}