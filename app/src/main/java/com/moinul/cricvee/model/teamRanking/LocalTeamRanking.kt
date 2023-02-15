package com.moinul.cricvee.model.teamRanking

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team_ranking_table", primaryKeys = ["gender","type","position"])
data class LocalTeamRanking(
    var gender: String,
    var type: String,
    var country_id: Int?,
    var team_id: Int?,
    var matches: Int?,
    var points: Int?,
    var position: Int,
    var rating: Int?,
    var name: String?
){
    constructor():this(
        "",
        "",
        -1,
        -1,
        null,
        -1,
        -1,
        -1,
        ""
    )

    fun setGender(gender: String):LocalTeamRanking{
        this.gender = gender
        return this
    }

    fun setType(type: String):LocalTeamRanking{
        this.type = type
        return this
    }

    fun setCountryId(country_id: Int?):LocalTeamRanking{
        this.country_id = country_id
        return this
    }

    fun setTeamId(team_id: Int?):LocalTeamRanking{
        this.team_id = team_id
        return this
    }

    fun setMatches(matches: Int?):LocalTeamRanking{
        this.matches = matches
        return this
    }

    fun setPoints(points: Int?):LocalTeamRanking{
        this.points = points
        return this
    }

    fun setPosition(position: Int):LocalTeamRanking{
        this.position = position
        return this
    }
    fun setRating(rating: Int?):LocalTeamRanking{
        this.rating = rating
        return this
    }
    fun setName(name: String):LocalTeamRanking{
        this.name = name
        return this
    }

}
