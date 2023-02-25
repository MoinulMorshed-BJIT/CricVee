package com.moinul.cricvee.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.moinul.cricvee.database.SportsDao
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.currentPlayers.SquadPlayerData
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.league.LeagueData
import com.moinul.cricvee.model.officials.OfficialsData
import com.moinul.cricvee.model.season.SeasonData
import com.moinul.cricvee.model.stage.StageData
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.model.venue.VenueData

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
    var readAllLeagueData: LiveData<List<LeagueData>> = sportsDao.readAllLeagueData()
    var readAllVenueData: LiveData<List<VenueData>> = sportsDao.readAllVenueData()
    var readAllSeasonData: LiveData<List<SeasonData>> = sportsDao.readAllSeasonData()
    var readAllStageData: LiveData<List<StageData>> = sportsDao.readAllStageData()





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
    fun insertAllLeagues(leagueList: List<LeagueData>){
        sportsDao.insertAllLeagueData(leagueList)
    }
    fun insertAllVenues(venueList: List<VenueData>){
        sportsDao.insertAllVenueData(venueList)
    }
    fun insertAllSeasons(seasonList: List<SeasonData>){
        sportsDao.insertAllSeasonData(seasonList)
    }
    fun insertAllStages(stageList: List<StageData>){
        sportsDao.insertAllStageData(stageList)
    }
    fun insertAllOfficials(officialsList: List<OfficialsData>){
        sportsDao.insertAllOfficials(officialsList)
    }

    fun deleteAllTeams(){
        sportsDao.deleteAllTeams()
    }

    fun readTeamById(teamId: Int):TeamData{
        return sportsDao.readTeamById(teamId)
    }

    fun readFixtureById(fixtureId: Int): FixtureData{
        return sportsDao.readFixtureById(fixtureId)
    }

    fun readOfficialById(officialId: Int): OfficialsData{
        return sportsDao.readOfficialById(officialId)
    }

    fun readStagesByLeagueId(leagueId: Int, seasonId: Int):LiveData<List<StageData>>{
        return sportsDao.readStagesByLeagueId(leagueId, seasonId)
    }

    fun readFixturesByStageId(stageId: Int):LiveData<List<FixtureData>>{
        return sportsDao.readFixturesByStageId(stageId)
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

    fun readLeagueById(leagueId: Int): LeagueData{
        return sportsDao.readLeagueById(leagueId)
    }

    fun readVenueById(venueId: Int):VenueData{
        return sportsDao.readVenueById(venueId)
    }

    fun readSeasonById(seasonId: Int):SeasonData{
        return sportsDao.readSeasonById(seasonId)
    }

    fun readStageById(stageId: Int):StageData{
        return sportsDao.readStageById(stageId)
    }


}