package com.moinul.cricvee.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teams.TeamData

@Dao
interface SportsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllFixtures(fixtureList: List<FixtureData>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllTeams(teamList: List<TeamData>)


}