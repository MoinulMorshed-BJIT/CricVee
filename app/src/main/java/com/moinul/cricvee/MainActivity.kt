package com.moinul.cricvee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.moinul.cricvee.viewmodel.SportsViewModel

class MainActivity : AppCompatActivity() {
    val viewModel: SportsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.fetchAllFixtures()
        viewModel.fetchAllTeams()
    }
}