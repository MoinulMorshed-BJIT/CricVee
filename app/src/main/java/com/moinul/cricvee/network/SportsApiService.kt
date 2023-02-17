package com.moinul.cricvee.network

import com.moinul.cricvee.model.countries.Countries
import com.moinul.cricvee.model.currentPlayers.CurrentSquad
import com.moinul.cricvee.model.fixtures.AllFixtures
import com.moinul.cricvee.model.fixtures.FixtureWithRun
import com.moinul.cricvee.model.fixturesWithScoreboard.FixtureWithScoreboard
import com.moinul.cricvee.model.teamRanking.TeamRanking
import com.moinul.cricvee.model.teams.AllTeams
import com.moinul.cricvee.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


/*val gson = GsonBuilder().create()
val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()*/

interface SportsApiService {

    @GET("fixtures")
    suspend fun fetchAllFixtures(
        @Query("page") currentPage: Int,
        @Query("api_token") api_token: String = Constants.API_KEY
    ): AllFixtures

    @GET("fixtures")
    suspend fun fetchTrendingFixtures(
        @Query("filter[starts_between]") duration: String,
        @Query("api_token") api_token: String = Constants.API_KEY
    ): AllFixtures


    @GET("teams")
    suspend fun fetchAllTeams(@Query("api_token") api_token: String = Constants.API_KEY): AllTeams

    @GET("fixtures/{fixtureId}")
    suspend fun fetchRunsByFixtureId(
        @Path("fixtureId") fixtureId: Int,
        @Query("include") includeParam: String = "runs",
        @Query("api_token") api_token: String = Constants.API_KEY
    ): FixtureWithRun

    @GET("teams/{teamId}/squad/23")
    suspend fun fetchCurrentSquad(
        @Path("teamId") teamId: Int,
        @Query("api_token") api_token: String = Constants.API_KEY
    ): CurrentSquad

    @GET("countries")
    suspend fun fetchCountries(@Query("api_token") api_token: String = Constants.API_KEY): Countries

    @GET("team-rankings")
    suspend fun fetchTeamRankings(@Query("api_token") api_token: String = Constants.API_KEY): TeamRanking

    @GET("fixtures/{fixtureId}")
    fun fetchScoreboardByFixtureId(
        @Path("fixtureId") fixtureId: Int,
        @Query("include") includeParam: String = "batting,bowling,lineup,balls,runs",
        @Query("api_token") api_token: String = Constants.API_KEY
    ): Call<FixtureWithScoreboard>


}

object SportsApi {
    val retrofitService: SportsApiService by lazy {
        retrofit.create(SportsApiService::class.java)
    }
}