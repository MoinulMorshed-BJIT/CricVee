package com.moinul.cricvee.model.stage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stage_table")
data class StageData(
    val code: String?,
    @PrimaryKey
    val id: Int,
    val league_id: Int?,
    val name: String?,
    val resource: String?,
    val season_id: Int?,
    val standings: Boolean?,
    val type: String?,
    val updated_at: String?
)