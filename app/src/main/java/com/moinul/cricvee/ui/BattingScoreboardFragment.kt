package com.moinul.cricvee.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.BattingScoreAdapter
import com.moinul.cricvee.adapter.BowlingScoreAdapter
import com.moinul.cricvee.adapter.MatchAdapter
import com.moinul.cricvee.databinding.FragmentBattingScoreboardBinding
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel

private const val TAG = "BattingScoreboardFragment"

class BattingScoreboardFragment() : Fragment() {
    private lateinit var binding: FragmentBattingScoreboardBinding
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var battingScoreRecyclerView: RecyclerView
    private lateinit var bowlingScoreRecyclerView: RecyclerView

    private var currentTeamInningsTabIndex = 1


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

        bowlingScoreRecyclerView = binding.bowlingScoreRecyclerView
        bowlingScoreRecyclerView.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectInningsTab()
        initialiseLayoutManager()

        binding.inningsTeam1.setOnClickListener{
            currentTeamInningsTabIndex = 1
            selectInningsTab()
            updateRecyclerView()
        }

        binding.inningsTeam2.setOnClickListener{
            currentTeamInningsTabIndex = 2
            selectInningsTab()
            updateRecyclerView()
        }
    }
    @SuppressLint("LongLogTag")
    fun initialiseLayoutManager(){
        battingScoreRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bowlingScoreRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        Log.d(TAG, "initialiseLayoutManager: CALLED")
        updateRecyclerView()
    }

    private fun updateRecyclerView(){
        observeData(currentTeamInningsTabIndex)
    }

    @SuppressLint("LongLogTag")
    private fun observeData(scorecardIndex: Int) {

        viewModel.getFixtureWithScoreboard(UtilTools.CLICKED_FIXTURE_ID)

        Log.d(TAG, "observeData: ${viewModel.fixtureWithScoreboard.value}")
        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner){
//            Log.d(TAG, "observeData: CALLED ${it}")
//            Log.d(TAG, "observeData: BALLS TEAM 1st NAME: ${it.data?.balls?.get(0)?.team?.code}")
//            Log.d(TAG, "observeData: BALLS TEAM last NAME: ${it.data?.balls?.last()?.team?.code}")
            binding.inningsTeam1.text = it.data?.balls?.get(0)?.team?.code+" Innings"
            binding.inningsTeam2.text = it.data?.balls?.last()?.team?.code+" Innings"

            battingScoreRecyclerView.adapter = BattingScoreAdapter(requireContext(), viewModel, it, scorecardIndex)
            bowlingScoreRecyclerView.adapter = BowlingScoreAdapter(requireContext(), viewModel, it, scorecardIndex)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun selectInningsTab(){
        when(currentTeamInningsTabIndex){
            1 -> {
                binding.inningsTeam1.isSelected = true
                binding.inningsTeam1.setBackgroundResource(R.color.colorSelectedTab)
                binding.inningsTeam1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTabIndicator))

                binding.inningsTeam2.isSelected = false
                binding.inningsTeam2.setBackgroundResource(R.drawable.tab_border_shape)
                binding.inningsTeam2.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))
            }
            2 -> {
                binding.inningsTeam2.isSelected = true
                binding.inningsTeam2.setBackgroundResource(R.color.colorSelectedTab)
                binding.inningsTeam2.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTabIndicator))

                binding.inningsTeam1.isSelected = false
                binding.inningsTeam1.setBackgroundResource(R.drawable.tab_border_shape)
                binding.inningsTeam1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))
            }
        }
    }

}
