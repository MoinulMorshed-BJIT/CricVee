package com.moinul.cricvee

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moinul.cricvee.utils.ConnectivityReceiver
import com.moinul.cricvee.viewmodel.SportsViewModel
const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() , ConnectivityReceiver.ConnectivityReceiverListener{
    private val viewModel: SportsViewModel by viewModels()

    private var receiver: ConnectivityReceiver? = null
    private lateinit var noInternetBar: TextView

    //private lateinit var viewModel: SportsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        receiver = ConnectivityReceiver()
        noInternetBar = findViewById(R.id.no_internet_bar)


        viewModel.internetStatus.observe(this, Observer { isConnected ->
            if (isConnected) {
                noInternetBar.visibility = View.GONE
                viewModel.fetchAllFixtures()
                viewModel.fetchAllTeams()
            } else {
                noInternetBar.visibility = View.VISIBLE
            }
        })

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
}