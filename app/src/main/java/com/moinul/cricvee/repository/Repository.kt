package com.moinul.cricvee.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.moinul.cricvee.database.SportsDao
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.currentPlayers.SquadPlayerData
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teams.TeamData

private const val TAG = "Repository"
class Repository(private val sportsDao: SportsDao) {
    val readRecentFixtureData: LiveData<List<FixtureData>> = sportsDao.readRecentFixtureData()
    val readUpcomingFixtureData: LiveData<List<FixtureData>> = sportsDao.readUpcomingFixtureData()
    val readAllTeams: LiveData<List<TeamData>> = sportsDao.readAllTeams()
    val readAllTeamIdList: LiveData<List<Int>> = sportsDao.readAllTeamIdList()
    val readAllSquadPlayers: LiveData<List<Squad>> = sportsDao.readAllSquadPlayers()
    val readTestRankingMen: LiveData<List<LocalTeamRanking>> = sportsDao.readTestRankingMen()
    var readODIRankingMen: LiveData<List<LocalTeamRanking>> =sportsDao.readODIRankingMen()
    var readT20IRankingMen: LiveData<List<LocalTeamRanking>> = sportsDao.readT20IRankingMen()

    var readTestRankingWomen: LiveData<List<LocalTeamRanking>> = sportsDao.readTestRankingWomen()
    var readODIRankingWomen: LiveData<List<LocalTeamRanking>> = sportsDao.readODIRankingWomen()
    var readT20IRankingWomen: LiveData<List<LocalTeamRanking>> = sportsDao.readT20IRankingWomen()
    suspend fun insertAllFixtures(fixtureList: List<FixtureData>){
        sportsDao.insertAllFixtures(fixtureList)
    }
    suspend fun deleteAllFixtures(){
        sportsDao.deleteAllFixtures()
    }

    suspend fun deleteAllTeamRankings(){
        sportsDao.deleteAllTeamRankings()
    }


    suspend fun insertAllTeams(teamList: List<TeamData>){
        sportsDao.insertAllTeams(teamList)
    }

    suspend fun insertAllCountries(countryList: List<CountryData>){
        sportsDao.insertAllCountries(countryList)
    }

    suspend fun insertTeamRankings(localTeamRankingList: List<LocalTeamRanking>){
        sportsDao.insertTeamRankings(localTeamRankingList)
    }

    fun deleteAllTeams(){
        sportsDao.deleteAllTeams()
    }

    fun readTeamById(teamId: Int):TeamData{
        return sportsDao.readTeamById(teamId)
    }

    fun getCountryIdByTeamName(teamName: String):Int{
        Log.d(TAG, "getCountryIdByTeamName: TeamName: $teamName Retrieved: ${sportsDao.getCountryIdByTeamName(teamName)}")
        return sportsDao.getCountryIdByTeamName(teamName)
    }

    fun getCountryById(countryId: Int):CountryData{
        return sportsDao.getCountryById(countryId)
    }

    fun getCountryByName(countryName: String):CountryData{
        return sportsDao.getCountryByName(countryName)
    }

    fun getTeamByName(teamName: String):TeamData{
        return sportsDao.getTeamByName(teamName)
    }

    suspend fun insertAllSquadPlayers(playersList :List<Squad>){
        return sportsDao.insertAllSquadPlayers(playersList)
    }


}