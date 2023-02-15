package com.moinul.cricvee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moinul.cricvee.model.countries.Countries
import com.moinul.cricvee.model.countries.CountryData
import com.moinul.cricvee.model.currentPlayers.Squad
import com.moinul.cricvee.model.fixtures.FixtureData
import com.moinul.cricvee.model.teamRanking.LocalTeamRanking
import com.moinul.cricvee.model.teams.TeamData

@Database(entities = [FixtureData::class, TeamData::class, Squad::class, CountryData::class, LocalTeamRanking::class], version = 5, exportSchema = false)
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