package com.moinul.cricvee.repository

import androidx.lifecycle.LiveData
import com.moinul.cricvee.database.SportsDao
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teams.TeamData

class Repository(private val sportsDao: SportsDao) {

    suspend fun insertAllFixtures(fixtureList: List<FixtureData>){
        sportsDao.insertAllFixtures(fixtureList)
    }

    suspend fun insertAllTeams(teamList: List<TeamData>){
        sportsDao.insertAllTeams(teamList)
    }
}