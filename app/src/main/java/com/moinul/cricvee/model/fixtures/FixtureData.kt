package com.moinul.cricvee.model.fixtures

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "fixture_table")
data class FixtureData(
    var draw_noresult: String?,
    @Ignore
    var elected: String?,
    @Ignore
    var first_umpire_id: Int?,
    var follow_on: Boolean?,
    @PrimaryKey
    var id: Int,
    @Ignore
    var last_period: Any?,
    var league_id: Int?,
    var live: Boolean?,
    @Ignore
    var localteam_dl_data: LocalteamDlData?,
    var localteam_id: Int?,
    var man_of_match_id: Int?,
    var man_of_series_id: Int?,
    var note: String?,
    @Ignore
    var referee_id: Int?,
    @Ignore
    var resource: String?,
    @Ignore
    var round: String?,
    @Ignore
    var rpc_overs: String?,
    @Ignore
    var rpc_target: String?,
    var season_id: Int?,
    @Ignore
    var second_umpire_id: Int?,
    var stage_id: Int?,
    var starting_at: String?,
    var status: String?,
    var super_over: Boolean?,
    var toss_won_team_id: Int?,
    var total_overs_played: Int?,
    @Ignore
    var tv_umpire_id: Int?,
    var type: String?,
    var venue_id: Int?,
    @Ignore
    var visitorteam_dl_data: VisitorteamDlData?,
    var visitorteam_id: Int?,
    @Ignore
    var weather_report: List<Any>?,
    var winner_team_id: Int?
){
    constructor():this(
        null,
        null,
        null,
        null,
        0,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    ){

    }
}