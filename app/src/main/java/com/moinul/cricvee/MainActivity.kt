package com.moinul.cricvee

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.moinul.cricvee.databinding.ActivityMainBinding
import com.moinul.cricvee.ui.FixtureListFragment
import com.moinul.cricvee.utils.ConnectivityReceiver
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*


const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() , ConnectivityReceiver.ConnectivityReceiverListener{
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var receiver: ConnectivityReceiver? = null
    private lateinit var noInternetBar: TextView



    //private lateinit var viewModel: SportsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = ConnectivityReceiver()
        noInternetBar = findViewById(R.id.no_internet_bar)
        viewModel.internetStatus.observe(this, Observer { isConnected ->
            if (isConnected) {
                noInternetBar.visibility = View.GONE

                viewModel.fetchTrendingFixtures(UtilTools.DURATION)
                viewModel.fetchTrendingFixtures(UtilTools.UPCOMING_DURATION)
                viewModel.fetchAllTeams()
                viewModel.fetchCountries()

                viewModel.fetchTeamRankings()
                viewModel.fetchLeagues()
                viewModel.fetchVenues()
                viewModel.fetchSeasons()
                viewModel.fetchStages()

            } else {
                noInternetBar.visibility = View.VISIBLE
            }
        })


        val bottomNavBar = binding.bottomNavbar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)
        binding.bottomNavbar.setupWithNavController(navController)


        bottomNavBar.setOnItemSelectedListener{
            when(it.itemId){

                R.id.home_bottom_nav -> {
                    findNavController(R.id.nav_host_fragment_container).navigate(R.id.homeFragment)
                }
                R.id.fixtures_bottom_nav ->{
                    findNavController(R.id.nav_host_fragment_container).navigate(R.id.fixtureListFragment)
                }

                R.id.players_bottom_nav -> {
                    findNavController(R.id.nav_host_fragment_container).navigate(R.id.playersFragment)
                }
                R.id.statistics_bottom_nav -> {
                    findNavController(R.id.nav_host_fragment_container).navigate(R.id.statisticsFragment)
                }

                else -> { }
            }
            true
        }
    }
    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
        registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.d(TAG, "onNetworkConnectionChanged: called")
        viewModel.updateInternetStatus(isConnected)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}