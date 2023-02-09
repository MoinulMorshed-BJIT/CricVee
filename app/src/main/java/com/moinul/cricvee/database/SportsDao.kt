package com.moinul.cricvee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teams.TeamData

@Dao
interface SportsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFixtures(fixtureList: List<FixtureData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTeams(teamList: List<TeamData>)

    @Query("select * from fixture_table order by starting_at desc limit 7")
    fun readRecentFixtureData():LiveData<List<FixtureData>>

    @Query("select * from team_table where id= :teamId")
    fun readTeamById(teamId: Int):TeamData

}