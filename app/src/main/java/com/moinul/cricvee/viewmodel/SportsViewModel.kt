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
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.fixtures.FixtureRunData
import com.moinul.cricvee.model.fixtures.FixtureWithRun
import com.moinul.cricvee.model.fixtures.Run
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.network.SportsApi
import com.moinul.cricvee.repository.Repository
import com.moinul.cricvee.utils.ConnectivityReceiver
import kotlinx.coroutines.*

class SportsViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository
    lateinit var currentFixtureWithRun: FixtureWithRun
    lateinit var currentFixtureRunData: FixtureRunData
    private val _internetStatus = MutableLiveData<Boolean>()
    val internetStatus: LiveData<Boolean>
        get() = _internetStatus

    var readRecentFixtureData: LiveData<List<FixtureData>>
    init {
        _internetStatus.value = ConnectivityReceiver.isConnected(application.applicationContext)

        val sportsDao = SportsDatabase.getDatabase(application).getDao()
        repository = Repository(sportsDao)

        readRecentFixtureData = repository.readRecentFixtureData
    }

    fun fetchAllFixtures(){
        viewModelScope.launch(Dispatchers.IO) {
            val fixtureList = SportsApi.retrofitService.fetchAllFixtures().data
            repository.insertAllFixtures(fixtureList)
        }
    }

    fun fetchAllTeams(){
        viewModelScope.launch(Dispatchers.IO) {
            val teamList = SportsApi.retrofitService.fetchAllTeams().data
            repository.insertAllTeams(teamList)
        }
    }

    /*fun fetchData(fixtureId: Int, callback: (FixtureWithRun) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = fetchRunsByFixtureId(fixtureId)
            withContext(Dispatchers.Main) {
                callback(data)
            }
        }
    }*/

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

    fun readTeamById(teamId: Int):TeamData{
        return repository.readTeamById(teamId)
    }



    fun updateInternetStatus(isConnected: Boolean) {
        _internetStatus.value = isConnected
    }
}