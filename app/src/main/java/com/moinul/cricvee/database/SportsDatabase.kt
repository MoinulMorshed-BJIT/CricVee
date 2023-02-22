package com.moinul.cricvee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moinul.cricvee.model.countries.Countries
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.league.LeagueData
import com.moinul.cricvee.model.season.SeasonData
import com.moinul.cricvee.model.stage.StageData
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teams.TeamData
import com.moinul.cricvee.model.venue.VenueData

@Database(entities = [FixtureData::class,
    TeamData::class, Squad::class, CountryData::class,
    LocalTeamRanking::class, LeagueData::class, StageData::class,
    VenueData::class, SeasonData::class], version = 7, exportSchema = false)
abstract class SportsDatabase :RoomDatabase(){
    abstract fun getDao(): SportsDao

    companion object {
        @Volatile
        private var INSTANCE: SportsDatabase? = null

        fun getDatabase(context: Context): SportsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, SportsDatabase::class.java, "sports_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }

    }
}