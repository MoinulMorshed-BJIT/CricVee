package com.moinul.cricvee.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.BattingScoreAdapter
import com.moinul.cricvee.adapter.BowlingScoreAdapter
import com.moinul.cricvee.databinding.FragmentBattingScoreboardBinding
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.fragment_batting_scoreboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        showProgressBar()
        viewModel.getFixtureWithScoreboard(UtilTools.CLICKED_FIXTURE_ID)
        Log.d(TAG, "observeData: ${viewModel.fixtureWithScoreboard.value}")
        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner){
            if(it.data?.status=="Finished"){
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val firstInningsTeam = it.data?.batting?.first()?.team_id?.let { it1 ->
                            viewModel.readTeamById(
                                it1
                            )
                        }
                        val secondInningsTeam = it.data?.batting?.last()?.team_id?.let { it1 ->
                            viewModel.readTeamById(
                                it1
                            )
                        }
                        GlobalScope.launch(Dispatchers.Main){
                            binding.inningsTeam1.text = firstInningsTeam?.code+" Innings"
                            binding.inningsTeam2.text = secondInningsTeam?.code+" Innings"
                        }
                    }catch (e: Exception){
                        Log.d(TAG, "observeData: $e")
                    }
                }
                if(it!=null){
                    binding.failureTxtV.visibility = View.VISIBLE
                    battingScoreRecyclerView.adapter = BattingScoreAdapter(requireContext(), viewModel, it, scorecardIndex)
                    bowlingScoreRecyclerView.adapter = BowlingScoreAdapter(requireContext(), viewModel, it, scorecardIndex)
                }
                hideProgressBar()
            }

        }
        binding.failureTxtV.visibility = View.GONE

    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
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
