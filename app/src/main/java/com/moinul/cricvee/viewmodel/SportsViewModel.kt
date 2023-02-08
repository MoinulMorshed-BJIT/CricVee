package com.moinul.cricvee.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moinul.cricvee.database.SportsDao
import com.moinul.cricvee.database.SportsDatabase
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.network.SportsApi
import com.moinul.cricvee.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SportsViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository

    init {
        val sportsDao = SportsDatabase.getDatabase(application).getDao()
        repository = Repository(sportsDao)
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

}