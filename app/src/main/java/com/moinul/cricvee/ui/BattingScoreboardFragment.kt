package com.moinul.cricvee.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.adapter.BattingScoreAdapter
import com.moinul.cricvee.adapter.MatchAdapter
import com.moinul.cricvee.databinding.FragmentBattingScoreboardBinding
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel

private const val TAG = "BattingScoreboardFragment"

class BattingScoreboardFragment() : Fragment() {
    private lateinit var binding: FragmentBattingScoreboardBinding
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var battingScoreRecyclerView: RecyclerView
    private lateinit var batting2ScoreRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBattingScoreboardBinding.inflate(layoutInflater)
        battingScoreRecyclerView = binding.battingScoreRecyclerView
        battingScoreRecyclerView.setHasFixedSize(true)

        batting2ScoreRecyclerView = binding.batting2ScoreRecyclerView
        batting2ScoreRecyclerView.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseLayoutManager()
    }
    @SuppressLint("LongLogTag")
    fun initialiseLayoutManager(){
        battingScoreRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        batting2ScoreRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        Log.d(TAG, "initialiseLayoutManager: CALLED")
        observeData()
    }

    @SuppressLint("LongLogTag")
    private fun observeData() {

        viewModel.getFixtureWithScoreboard(UtilTools.CLICKED_FIXTURE_ID)

        Log.d(TAG, "observeData: ${viewModel.fixtureWithScoreboard.value}")
        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner){
            Log.d(TAG, "observeData: CALLED ${it}")
            Log.d(TAG, "observeData: BALLS TEAM 1st NAME: ${it.data?.balls?.get(0)?.team?.code}")
            Log.d(TAG, "observeData: BALLS TEAM last NAME: ${it.data?.balls?.last()?.team?.code}")
            binding.firstInningsTeam.text = it.data?.balls?.get(0)?.team?.code+" Innings"
            binding.secondInningsTeam.text = it.data?.balls?.last()?.team?.code+" Innings"

            battingScoreRecyclerView.adapter = BattingScoreAdapter(requireContext(), viewModel, it, 1)
            batting2ScoreRecyclerView.adapter = BattingScoreAdapter(requireContext(), viewModel, it, 2)
        }
    }

}
