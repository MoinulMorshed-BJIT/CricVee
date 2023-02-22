package com.moinul.cricvee.ui

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
import com.moinul.cricvee.adapter.RankingAdapter
import com.moinul.cricvee.databinding.FragmentICCTeamRankingsBinding
import com.moinul.cricvee.databinding.FragmentICCWomenTeamRankingsBinding
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_i_c_c_team_rankings.*

private const val TAG = "ICCWomenTeamRankingsFra"

class ICCWomenTeamRankingsFragment : Fragment() {
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var binding: FragmentICCWomenTeamRankingsBinding
    private lateinit var teamRankingRecyclerView: RecyclerView
    private var currentFormatTabIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentICCWomenTeamRankingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottom_navbar.visibility=View.GONE

        selectFormatTab()


        teamRankingRecyclerView = binding.teamRankingRecyclerView
        teamRankingRecyclerView.setHasFixedSize(true)
        initialiseLayoutManager()


        binding.typeText.setOnClickListener{
            currentFormatTabIndex = 1
            selectFormatTab()
            updateRecyclerView()
        }
        binding.typeText2.setOnClickListener{
            currentFormatTabIndex = 2
            selectFormatTab()
            updateRecyclerView()
        }

    }

    private fun initialiseLayoutManager() {
        teamRankingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        updateRecyclerView()
    }

    private fun updateRecyclerView(){
        when(currentFormatTabIndex){
            1 -> observeWomenODIData()
            2 -> observeWomenT20IData()
        }
    }

    private fun observeMenTestData() {
        viewModel.readTestRankingMen.observe(viewLifecycleOwner){
            val adapterScrollState = team_ranking_recyclerView.layoutManager?.onSaveInstanceState()
            teamRankingRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            teamRankingRecyclerView.adapter = RankingAdapter(requireContext(), viewModel, it)
        }
    }

    private fun observeMenODIData() {
        viewModel.readODIRankingMen.observe(viewLifecycleOwner){
            val adapterScrollState = team_ranking_recyclerView.layoutManager?.onSaveInstanceState()
            teamRankingRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            teamRankingRecyclerView.adapter = RankingAdapter(requireContext(), viewModel, it)
        }
    }

    private fun observeMenT20IData() {
        viewModel.readT20IRankingMen.observe(viewLifecycleOwner){
            val adapterScrollState = team_ranking_recyclerView.layoutManager?.onSaveInstanceState()
            teamRankingRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            teamRankingRecyclerView.adapter = RankingAdapter(requireContext(), viewModel, it)
        }
    }

    private fun observeWomenTestData() {
        viewModel.readTestRankingWomen.observe(viewLifecycleOwner){
            val adapterScrollState = team_ranking_recyclerView.layoutManager?.onSaveInstanceState()
            teamRankingRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            teamRankingRecyclerView.adapter = RankingAdapter(requireContext(), viewModel, it)
        }
    }

    private fun observeWomenODIData() {
        viewModel.readODIRankingWomen.observe(viewLifecycleOwner){
            val adapterScrollState = team_ranking_recyclerView.layoutManager?.onSaveInstanceState()
            teamRankingRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            teamRankingRecyclerView.adapter = RankingAdapter(requireContext(), viewModel, it)
        }
    }

    private fun observeWomenT20IData() {
        viewModel.readT20IRankingWomen.observe(viewLifecycleOwner){
            val adapterScrollState = team_ranking_recyclerView.layoutManager?.onSaveInstanceState()
            teamRankingRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            teamRankingRecyclerView.adapter = RankingAdapter(requireContext(), viewModel, it)
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().bottom_navbar.visibility=View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity().bottom_navbar.visibility = View.VISIBLE
    }


    fun selectFormatTab(){
        when(currentFormatTabIndex){
            1 -> {
                binding.typeText.isSelected = true
                binding.typeText.setBackgroundResource(R.color.colorSelectedTab)
                binding.typeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTabIndicator))

                binding.typeText2.isSelected = false
                binding.typeText2.setBackgroundResource(R.drawable.tab_border_shape)
                binding.typeText2.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))

            }
            2 -> {
                binding.typeText2.isSelected = true
                binding.typeText2.setBackgroundResource(R.color.colorSelectedTab)
                binding.typeText2.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTabIndicator))

                binding.typeText.isSelected = false
                binding.typeText.setBackgroundResource(R.drawable.tab_border_shape)
                binding.typeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))

            }
        }
    }


}