package com.moinul.cricvee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.currentPlayers.SquadPlayerData
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teamRanking.TeamRanking
import com.moinul.cricvee.model.teams.TeamData

@Dao
interface SportsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFixtures(fixtureList: List<FixtureData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTeams(teamList: List<TeamData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSquadPlayers(playersList: List<Squad>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCountries(countryList: List<CountryData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamRankings(localTeamRankingList: List<LocalTeamRanking>)

    @Query("delete from team_ranking_table")
    fun deleteAllTeamRankings()


    @Query("select * from team_ranking_table where type='TEST' and gender='men' order by position")
    fun readTestRankingMen():LiveData<List<LocalTeamRanking>>

    @Query("select * from team_ranking_table where type='ODI' and gender='men' order by position")
    fun readODIRankingMen(): LiveData<List<LocalTeamRanking>>
    @Query("select * from team_ranking_table where type='T20I' and gender='men' order by position")
    fun readT20IRankingMen(): LiveData<List<LocalTeamRanking>>
    @Query("select * from team_ranking_table where type='TEST' and gender='women' order by position")
    fun readTestRankingWomen(): LiveData<List<LocalTeamRanking>>
    @Query("select * from team_ranking_table where type='ODI' and gender='women' order by position")
    fun readODIRankingWomen(): LiveData<List<LocalTeamRanking>>
    @Query("select * from team_ranking_table where type='T20I' and gender='women' order by position")
    fun readT20IRankingWomen(): LiveData<List<LocalTeamRanking>>

    @Query("select * from fixture_table where status='Finished' order by starting_at desc limit 7")
    fun readRecentFixtureData():LiveData<List<FixtureData>>

    @Query("select * from team_table where id= :teamId")
    fun readTeamById(teamId: Int):TeamData

    @Query("select id from country_table where name=:teamName")
    fun getCountryIdByTeamName(teamName: String):Int

    @Query("select * from country_table where id=:countryId ")
    fun getCountryById(countryId: Int):CountryData

    @Query("select * from country_table where name= :countryName ")
    fun getCountryByName(countryName: String):CountryData

    @Query("select * from team_table where name= :teamName ")
    fun getTeamByName(teamName: String):TeamData



    @Query("delete from fixture_table")
    fun deleteAllFixtures()

    @Query("delete from team_table")
    fun deleteAllTeams()

    @Query("select * from team_table")
    fun readAllTeams():LiveData<List<TeamData>>

    @Query("select id from team_table where national_team=1 ")
    fun readAllTeamIdList():LiveData<List<Int>>

    @Query("select * from current_players_table")
    fun readAllSquadPlayers():LiveData<List<Squad>>


}