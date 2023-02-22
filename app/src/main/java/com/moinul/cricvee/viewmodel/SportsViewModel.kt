package com.moinul.cricvee.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.moinul.cricvee.MyApplication
import com.moinul.cricvee.database.SportsDao
import com.moinul.cricvee.database.SportsDatabase
import com.moinul.cricvee.model.career.Career
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.fixtures.FixtureRunData
import com.moinul.cricvee.model.fixtures.FixtureWithRun
import com.moinul.cricvee.model.fixtures.Run
import com.moinul.cricvee.model.fixturesWithScoreboard.FixtureScoreboardData
import com.moinul.cricvee.model.fixturesWithScoreboard.FixtureWithScoreboard
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.network.SportsApi
import com.moinul.cricvee.repository.Repository
import com.moinul.cricvee.utils.ConnectivityReceiver
import com.moinul.cricvee.utils.Constants
import kotlinx.coroutines.*
import retrofit2.await
import kotlin.math.log

private const val TAG = "SportsViewModel"
class SportsViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository
    private val _internetStatus = MutableLiveData<Boolean>()

    val internetStatus: LiveData<Boolean>
        get() = _internetStatus



    var readRecentFixtureData: LiveData<List<FixtureData>>
    var readUpcomingFixtureData: LiveData<List<FixtureData>>
    var readAllSquadPlayers: LiveData<List<Squad>>
    var readAllTeams: LiveData<List<TeamData>>
    var readAllTeamIdList: LiveData<List<Int>>
    var readTestRankingMen: LiveData<List<LocalTeamRanking>>
    var readODIRankingMen: LiveData<List<LocalTeamRanking>>
    var readT20IRankingMen: LiveData<List<LocalTeamRanking>>

    var readTestRankingWomen: LiveData<List<LocalTeamRanking>>
    var readODIRankingWomen: LiveData<List<LocalTeamRanking>>
    var readT20IRankingWomen: LiveData<List<LocalTeamRanking>>


    init {
        _internetStatus.value = ConnectivityReceiver.isConnected(application.applicationContext)

        val sportsDao = SportsDatabase.getDatabase(application).getDao()
        repository = Repository(sportsDao)

        readRecentFixtureData = repository.readRecentFixtureData
        readUpcomingFixtureData = repository.readUpcomingFixtureData
        readAllSquadPlayers = repository.readAllSquadPlayers
        readAllTeams = repository.readAllTeams
        readAllTeamIdList = repository.readAllTeamIdList
        readTestRankingMen = repository.readTestRankingMen
        readODIRankingMen = repository.readODIRankingMen
        readT20IRankingMen = repository.readT20IRankingMen
        readTestRankingWomen = repository.readTestRankingWomen
        readODIRankingWomen = repository.readODIRankingWomen
        readT20IRankingWomen = repository.readT20IRankingWomen
    }

    fun fetchAllFixtures(){
        viewModelScope.launch(Dispatchers.IO) {
            var lastPage = 0
            try {
                lastPage = SportsApi.retrofitService.fetchAllFixtures(1).meta?.last_page!!
                if(lastPage!=null){
                    for(currentPage in 1..lastPage){
                        val fixtureList = SportsApi.retrofitService.fetchAllFixtures(currentPage).data
                        try{
                            if(fixtureList.isNotEmpty()) {
                                //repository.deleteAllFixtures()
                                repository.insertAllFixtures(fixtureList)
                            }
                        }catch (e:Exception){
                            Log.d("SportsViewModel", "fetchAllFixtures: fixtureList fetch $e")
                        }
                    }
                }


            }catch (e: Exception){
                Log.d(TAG, "fetchAllFixtures: lastPage fetch $e ")
            }


        }
    }

    fun fetchTrendingFixtures(duration: String){
        viewModelScope.launch(Dispatchers.IO) {

            try{
                val fixtureList = SportsApi.retrofitService.fetchTrendingFixtures(duration).data
                if(fixtureList.isNotEmpty()) {
                    //repository.deleteAllFixtures()
                    repository.insertAllFixtures(fixtureList)
                }
            }catch (e:Exception){
                Log.d("SportsViewModel", "fetchTrendingFixtures: $e")
            }
        }
    }

    fun fetchAllTeams(){
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val teamList = SportsApi.retrofitService.fetchAllTeams().data
                //repository.deleteAllTeams()
                repository.insertAllTeams(teamList)
            }catch (e: Exception){
                Log.d("SportsViewModel", "fetchAllTeams: $e")
            }
        }
    }

    fun fetchCountries(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val countryList = SportsApi.retrofitService.fetchCountries().data
                repository.insertAllCountries(countryList)
            }catch (e: Exception){
                Log.d(TAG, "fetchCountries: $e")
            }
        }
    }

    fun fetchTeamRankings(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val teamRankingDataList = SportsApi.retrofitService.fetchTeamRankings().data
                val mutableListOfTeamRanking : MutableList<LocalTeamRanking> = mutableListOf()
                for (teamRankData in teamRankingDataList){

                    for(team in teamRankData.team){
                        var localTeamRanking = LocalTeamRanking()
                        var nameOfTeamOrCountry = team.name.toString()
                        //Log.d(TAG, "fetchTeamRankings: NAME: $nameOfTeamOrCountry")

                        if(teamRankData.gender=="women"){
                            Log.d(TAG, "fetchTeamRankings: NAME: $nameOfTeamOrCountry")
                            Log.d(TAG, "fetchTeamRankings: Retrieved CountryID: ${repository.getCountryIdByTeamName(nameOfTeamOrCountry)}")
                            nameOfTeamOrCountry = nameOfTeamOrCountry.substring(0, nameOfTeamOrCountry.lastIndex-1)
                            Log.d(TAG, "fetchTeamRankings: NAME: $nameOfTeamOrCountry")
                        }



                        localTeamRanking = localTeamRanking.setGender(teamRankData.gender!!)
                            .setType(teamRankData.type!!)
                            .setCountryId(repository.getCountryIdByTeamName(nameOfTeamOrCountry))
                            .setTeamId(team.id)
                            .setPosition(team.ranking?.position!!)
                            .setMatches(team.ranking?.matches)
                            .setPoints(team.ranking?.points)
                            .setRating(team.ranking?.rating)
                            .setName(nameOfTeamOrCountry)
                        mutableListOfTeamRanking.add(localTeamRanking)
                    }
                }
                repository.deleteAllTeamRankings()
                repository.insertTeamRankings(mutableListOfTeamRanking)

            }catch (e: Exception){
                Log.d(TAG, "fetchTeamRankings: $e")
            }
        }
    }

    suspend fun fetchRunsByFixtureId(fixtureId: Int):Result<FixtureWithRun>{
        return viewModelScope.async(Dispatchers.IO) {
            try {
                val response = SportsApi.retrofitService.fetchRunsByFixtureId(fixtureId)
                Log.d("TAGviewModel", "SUCCESS fetchRunsByFixtureId: ${response.data}")
                Result.success(response)
            } catch (e: Exception) {
                Log.d("TAGviewModel", "API fetch fail $e")
                Result.failure(e)
            }
        }.await()
    }
    private val _fixtureWithScoreboard = MutableLiveData<FixtureWithScoreboard>()
    val fixtureWithScoreboard: LiveData<FixtureWithScoreboard>
        get() = _fixtureWithScoreboard
    suspend fun fetchScoreboardByFixtureId(fixtureId: Int):FixtureWithScoreboard{
        return SportsApi.retrofitService.fetchScoreboardByFixtureId(fixtureId).await()
    }

    fun getFixtureWithScoreboard(fixtureId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val fixtureWithScoreboard = fetchScoreboardByFixtureId(fixtureId)
                    Log.d(TAG, "getFixtureWithScoreboard: $fixtureWithScoreboard")
                    _fixtureWithScoreboard.postValue(fixtureWithScoreboard)
                    Log.d(TAG, "getFixtureWithScoreboard: ${_fixtureWithScoreboard.value}")
                }catch (e: Exception){
                    Log.d(TAG, "getFixtureWithScoreboard: $e")
                }

            }

        }
    }

    private val _playerCareer = MutableLiveData<Career>()
    val playerCareer: LiveData<Career>
        get() = _playerCareer
    suspend fun fetchCareerByPlayerId(playerId: Int): Career {
        return SportsApi.retrofitService.fetchCareerByPlayerId(playerId).await()
    }


    fun getCareerByPlayerId(playerId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val playerCareerLocal = fetchCareerByPlayerId(playerId)
                    Log.d(TAG, "getCareerByPlayerId: $playerCareerLocal")
                    _playerCareer.postValue(playerCareerLocal)
                    Log.d(TAG, "getCareerByPlayerId: ${_playerCareer.value}")
                }catch (e: Exception){
                    Log.d(TAG, "getCareerByPlayerId: $e")
                }

            }

        }
    }


    fun readTeamById(teamId: Int):TeamData{
        return repository.readTeamById(teamId)
    }
    fun fetchCurrentSquad(teamIdList: List<Int>){
        Log.d(TAG, "fetchCurrentSquad: CALLED")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "STATE fetchCurrentSquad: ${repository.readAllSquadPlayers.value} ${teamIdList.isNotEmpty()}")
            if((repository.readAllSquadPlayers.value==null || repository.readAllSquadPlayers.value?.isEmpty() == true) && teamIdList.isNotEmpty()){
                for(teamId in teamIdList){
                    try {
                        val playersList = SportsApi.retrofitService.fetchCurrentSquad(teamId).data.squad

                        //repository.deleteAllTeams()
                        Log.d("SportsViewModel", "fetchCurrentSquad: SUCCESS ${playersList}")
                        repository.insertAllSquadPlayers(playersList)
                    }catch (e: Exception){
                        Log.d("SportsViewModel", "FAILED fetchCurrentSquad: $e")
                    }
                }
            }
        }
    }



    fun updateInternetStatus(isConnected: Boolean) {
        _internetStatus.value = isConnected
    }

    fun getCountryById(countryId: Int):CountryData{
        return repository.getCountryById(countryId)
    }

    fun getTeamByName(teamName: String):TeamData{
        return repository.getTeamByName(teamName)
    }

    fun getCountryByName(countryName: String):CountryData{
        return repository.getCountryByName(countryName)
    }
}