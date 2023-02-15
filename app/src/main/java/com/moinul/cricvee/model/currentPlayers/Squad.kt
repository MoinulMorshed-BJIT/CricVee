package com.moinul.cricvee.model.currentPlayers

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "current_players_table")
data class Squad(
    var battingstyle: String?,
    var bowlingstyle: String?,
    var country_id: Int?,
    var dateofbirth: String?,
    var firstname: String?,
    var fullname: String?,
    var gender: String?,
    @PrimaryKey
    var id: Int,
    var image_path: String?,
    var lastname: String?,
    @Ignore
    var position: Position?,
    var resource: String?,
    @Ignore
    var squad: SquadX?,
    var updated_at: String?
){
    constructor():this(
        null,
        null,
        null,
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
        null
    )
}
