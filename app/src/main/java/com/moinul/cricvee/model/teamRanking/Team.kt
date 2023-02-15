package com.moinul.cricvee.model.teamRanking

data class Team(
    val code: String?,
    val country_id: Int?,
    val id: Int?,
    val image_path: String?,
    val name: String?,
    val national_team: Boolean?,
    val position: Int?,
    val ranking: Ranking?,
    val resource: String?,
    val updated_at: String?
)