package com.moinul.cricvee.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.PlayerSearchAdapter
import com.moinul.cricvee.adapter.RankingAdapter
import com.moinul.cricvee.databinding.FragmentICCTeamRankingsBinding
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_i_c_c_team_rankings.*
import kotlinx.android.synthetic.main.fragment_players.*

private const val TAG = "ICCTeamRankingsFragment"
class ICCTeamRankingsFragment : Fragment() {
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var binding: FragmentICCTeamRankingsBinding
    private lateinit var teamRankingRecyclerView: RecyclerView
    private var currentGenderTabIndex = 1
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
        binding = FragmentICCTeamRankingsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottom_navbar.visibility=View.GONE

        selectGenderTab()
        selectFormatTab()


        teamRankingRecyclerView = binding.teamRankingRecyclerView
        teamRankingRecyclerView.setHasFixedSize(true)
        initialiseLayoutManager()

        binding.genderText.setOnClickListener{
            currentGenderTabIndex = 1
            selectGenderTab()
            updateRecyclerView()
        }

        binding.genderText2.setOnClickListener{
            currentGenderTabIndex = 2
            selectGenderTab()
            updateRecyclerView()
        }

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
        binding.typeText3.setOnClickListener{
            currentFormatTabIndex = 3
            selectFormatTab()
            updateRecyclerView()
        }

    }

    private fun initialiseLayoutManager() {
        teamRankingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        updateRecyclerView()
    }

    private fun updateRecyclerView(){
        when(currentGenderTabIndex){
            1 -> when(currentFormatTabIndex){
                    1 -> observeMenTestData()
                    2 -> observeMenODIData()
                    3 -> observeMenT20IData()
            }
            2 -> when(currentFormatTabIndex){
                    1 -> observeWomenTestData()
                    2 -> observeWomenODIData()
                    3 -> observeWomenT20IData()
            }
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

    fun selectGenderTab(){
        when(currentGenderTabIndex){
            1 -> {
                binding.genderText.isSelected = true
                binding.genderText.setBackgroundResource(R.color.colorSelectedTab)

                binding.genderText2.isSelected = false
                binding.genderText2.setBackgroundResource(R.drawable.tab_border_shape)
            }
            2 -> {
                binding.genderText2.isSelected = true
                binding.genderText2.setBackgroundResource(R.color.colorSelectedTab)

                binding.genderText.isSelected = false
                binding.genderText.setBackgroundResource(R.drawable.tab_border_shape)
            }
        }
    }

    fun selectFormatTab(){
        when(currentFormatTabIndex){
            1 -> {
                binding.typeText.isSelected = true
                binding.typeText.setBackgroundResource(R.color.colorSelectedTab)

                binding.typeText2.isSelected = false
                binding.typeText2.setBackgroundResource(R.drawable.tab_border_shape)
                binding.typeText3.isSelected = false
                binding.typeText3.setBackgroundResource(R.drawable.tab_border_shape)
            }
            2 -> {
                binding.typeText2.isSelected = true
                binding.typeText2.setBackgroundResource(R.color.colorSelectedTab)

                binding.typeText.isSelected = false
                binding.typeText.setBackgroundResource(R.drawable.tab_border_shape)
                binding.typeText3.isSelected = false
                binding.typeText3.setBackgroundResource(R.drawable.tab_border_shape)
            }
            3 -> {
                binding.typeText3.isSelected = true
                binding.typeText3.setBackgroundResource(R.color.colorSelectedTab)

                binding.typeText2.isSelected = false
                binding.typeText2.setBackgroundResource(R.drawable.tab_border_shape)
                binding.typeText.isSelected = false
                binding.typeText.setBackgroundResource(R.drawable.tab_border_shape)
            }
        }
    }


}