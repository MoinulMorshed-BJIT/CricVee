package com.moinul.cricvee.model.teams

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "team_table")
data class TeamData(
    var code: String?,
    var country_id: Int?,
    @PrimaryKey
    var id: Int,
    var image_path: String?,
    var name: String?,
    var national_team: Boolean?,
    @Ignore
    var resource: String?,
    var updated_at: String?
){
    constructor():this(
        null,
        null,
        0,
        null,
        null,
        null,
        null,
        null){

    }
}