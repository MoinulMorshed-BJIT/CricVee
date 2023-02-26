package com.moinul.cricvee.viewmodel

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeUnit
import android.icu.util.TimeZone
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.service.autofill.FieldClassification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
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
import com.moinul.cricvee.model.league.LeagueData
import com.moinul.cricvee.model.officials.OfficialsData
import com.moinul.cricvee.model.season.SeasonData
import com.moinul.cricvee.model.stage.StageData
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.model.venue.VenueData
import com.moinul.cricvee.network.SportsApi
import com.moinul.cricvee.repository.Repository
import com.moinul.cricvee.utils.ConnectivityReceiver
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.worker.MatchNotificationWorker
import kotlinx.coroutines.*
import retrofit2.await
import java.util.*
import kotlin.math.log

private const val TAG = "SportsViewModel"
class SportsViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository
    private val _internetStatus = MutableLiveData<Boolean>()

    val internetStatus: LiveData<Boolean>
        get() = _internetStatus
    private val handler = Handler(Looper.getMainLooper())



    var readRecentFixtureData: LiveData<List<FixtureData>>
    var readUpcomingFixtureData: LiveData<List<FixtureData>>
    var readAllSquadPlayers: LiveData<List<Squad>>
    var readAllTeams: LiveData<List<TeamData>>
    var readAllTeamIdList: LiveData<List<Int>>
    var readTestRankingMen: LiveData<List<LocalTeamRanking>>
    var readODIRankingMen: LiveData<List<LocalTeamRanking>>
    var readT20IRankingMen: LiveData<List<LocalTeamRanking>>
    var readAllLeagueData: LiveData<List<LeagueData>>
    var readAllVenueData: LiveData<List<VenueData>>
    var readAllSeasonData: LiveData<List<SeasonData>>
    var readAllStageData: LiveData<List<StageData>>

    var readTestRankingWomen: LiveData<List<LocalTeamRanking>>
    var readODIRankingWomen: LiveData<List<LocalTeamRanking>>
    var readT20IRankingWomen: LiveData<List<LocalTeamRanking>>
    private val countdownList = MutableLiveData<List<Long>>()

    private val workManager = WorkManager.getInstance(application)

    fun scheduleMatchNotifications() {
        countdownList.value?.let { listOfCountdown ->
            listOfCountdown.forEach{
                countdown ->
                run {
                    if (countdown > 0 && countdown <= 14160000 /*countdown <= 900000*/) {
                        val notificationWorkRequest =
                            OneTimeWorkRequestBuilder<MatchNotificationWorker>()
                                .setInitialDelay(
                                    60000,
                                    java.util.concurrent.TimeUnit.MILLISECONDS
                                )
                                .addTag("match_notification")
                                .build()
                        workManager.enqueue(notificationWorkRequest)
                    }
                }
            }
        }
    }


    init {
        //_internetStatus.value = ConnectivityReceiver.isConnected(application.applicationContext)
        _internetStatus.postValue(ConnectivityReceiver.isConnected(application.applicationContext))

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
        readAllLeagueData =repository.readAllLeagueData
        readAllVenueData = repository.readAllVenueData
        readAllSeasonData =repository.readAllSeasonData
        readAllStageData = repository.readAllStageData
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun calculateCountdown() {
        val currentMillis = System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())

        readUpcomingFixtureData.value?.let { upcomingFixtureDataList ->
            val countdownList = mutableListOf<Long>()
            upcomingFixtureDataList.forEach { fixtureData ->
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val date = formatter.parse(fixtureData.starting_at)
                val eventMillis = date.time
                val countdown = eventMillis - currentMillis
                countdownList.add(countdown)
            }
            this.countdownList.value = countdownList
            handler.postDelayed({ calculateCountdown() }, 1000)
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun getCountdownList(): LiveData<List<Long>> {
        calculateCountdown()
        scheduleMatchNotifications()
        return countdownList
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
    fun fetchLeagues(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val leagueList = SportsApi.retrofitService.fetchAllLeagues().data
                repository.insertAllLeagues(leagueList)
            }catch (e: Exception){
                Log.d(TAG, "fetchLeagues: $e")
            }
        }
    }
    fun fetchVenues(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val venueList = SportsApi.retrofitService.fetchAllVenues().data
                repository.insertAllVenues(venueList)
            }catch (e: Exception){
                Log.d(TAG, "fetchVenues: $e")
            }
        }
    }
    fun fetchSeasons(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val seasonList = SportsApi.retrofitService.fetchAllSeasons().data
                repository.insertAllSeasons(seasonList)
            }catch (e: Exception){
                Log.d(TAG, "fetchSeasons: $e")
            }
        }
    }
    fun fetchStages(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val stageList = SportsApi.retrofitService.fetchAllStages().data
                repository.insertAllStages(stageList)
            }catch (e: Exception){
                Log.d(TAG, "fetchStages: $e")
            }
        }
    }

    fun fetchOfficials(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val officialsList = SportsApi.retrofitService.fetchOfficials().data
                repository.insertAllOfficials(officialsList)
            }catch (e: Exception){
                Log.d(TAG, "fetchStages: $e")
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

    private val _currentFixture = MutableLiveData<FixtureData>()
    val currentFixture: LiveData<FixtureData>
        get() = _currentFixture
    fun readFixtureById(fixtureId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    _currentFixture.postValue(repository.readFixtureById(fixtureId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }


    fun readTeamById(teamId: Int):TeamData{
        return repository.readTeamById(teamId)
    }

    private val _currentTeam = MutableLiveData<TeamData>()
    val currentTeam: LiveData<TeamData>
    get() = _currentTeam

    fun readTeamByIdLive(teamId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    _currentTeam.postValue(repository.readTeamById(teamId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }




    private val _current1stUmpire= MutableLiveData<OfficialsData>()
    val current1stUmpire: LiveData<OfficialsData>
        get() = _current1stUmpire

    fun read1stUmpireById(firstUmpireId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    _current1stUmpire.postValue(repository.readOfficialById(firstUmpireId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }

    private val _current2ndUmpire = MutableLiveData<OfficialsData>()
    val current2ndUmpire: LiveData<OfficialsData>
        get() = _current2ndUmpire

    fun read2ndUmpireById(officialId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    _current2ndUmpire.postValue(repository.readOfficialById(officialId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }

    private val _currentTVUmpire = MutableLiveData<OfficialsData>()
    val currentTVUmpire: LiveData<OfficialsData>
        get() = _currentTVUmpire

    fun readTVUmpireById(officialId: Int){
        viewModelScope.launch{
            withContext(Dispatchers.IO) {
                try{
                    _currentTVUmpire.postValue(repository.readOfficialById(officialId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }

    private val _currentReferee = MutableLiveData<OfficialsData>()
    val currentReferee: LiveData<OfficialsData>
        get() = _currentReferee

    fun readRefereeById(officialId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    _currentReferee.postValue(repository.readOfficialById(officialId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }

    fun fetchCurrentSquad(teamIdList: List<Int>){
        Log.d(TAG, "fetchCurrentSquad: CALLED")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "STATE fetchCurrentSquad: ${repository.readAllSquadPlayers.value} ${teamIdList.isNotEmpty()}")
            if(repository.readAllSquadPlayers.value==null || repository.readAllSquadPlayers.value?.isEmpty() == true) {
                for(teamId in teamIdList){
                    try {
                        val playersList = SportsApi.retrofitService.fetchCurrentSquad(teamId).data.squad
                        Log.d("SportsViewModel", "fetchCurrentSquad: SUCCESS ${playersList}")
                        repository.insertAllSquadPlayers(playersList)
                    }catch (e: Exception){
                        Log.d("SportsViewModel", "FAILED fetchCurrentSquad: $e")
                    }
                }
            }
        }
    }


    fun readLeagueById(leagueId: Int): LeagueData{
        return repository.readLeagueById(leagueId)
    }

    private val _currentVenue = MutableLiveData<VenueData>()
    val currentVenue: LiveData<VenueData>
        get() = _currentVenue

    fun readVenueById(venueId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try{
                    _currentVenue.postValue(repository.readVenueById(venueId))
                }catch (e: Exception){
                    Log.d(TAG, "readFixtureById: $e")
                }

            }
        }
    }

    fun readSeasonById(seasonId: Int):SeasonData{
        return repository.readSeasonById(seasonId)
    }

    private val _currentStage = MutableLiveData<StageData>()
    val currentStage: LiveData<StageData>
        get() = _currentStage
    fun readStageById(stageId: Int):StageData{
        return repository.readStageById(stageId)
    }

    fun readStageByIdLive(stageId:Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _currentStage.postValue(repository.readStageById(stageId))
                }catch (e: Exception){
                    Log.d(TAG, "readStageById: $e")
                }
            }
        }
    }


    fun readStagesByLeagueId(leagueId: Int, seasonId: Int):LiveData<List<StageData>>{
        return repository.readStagesByLeagueId(leagueId, seasonId)
    }

    fun readFixturesByStageId(stageId: Int):LiveData<List<FixtureData>>{
        return repository.readFixturesByStageId(stageId)
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