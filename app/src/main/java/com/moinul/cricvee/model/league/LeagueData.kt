package com.moinul.cricvee.model.league

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "league_table")
data class LeagueData(
    val code: String?,
    val country_id: Int?,
    @PrimaryKey
    val id: Int,
    val image_path: String?,
    val name: String?,
    val resource: String?,
    val season_id: Int?,
    val type: String?,
    val updated_at: String?
)/*{
    constructor():this(null,
    null,
    0,
    null,
    null,
    null,
    null,
    null,
    null)
}*/