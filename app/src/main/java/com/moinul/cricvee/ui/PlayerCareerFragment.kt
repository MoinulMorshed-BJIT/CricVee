package com.moinul.cricvee.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.BattingCareerAdapter
import com.moinul.cricvee.adapter.BowlingCareerAdapter
import com.moinul.cricvee.databinding.FragmentPlayerCareerBinding
import com.moinul.cricvee.model.career.CareerX
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Calendar

private const val TAG = "PlayerCareerFragment"
class PlayerCareerFragment : Fragment() {
    private val args: PlayerCareerFragmentArgs by navArgs()
    private lateinit var binding: FragmentPlayerCareerBinding
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var skillPerformanceRecyclerView: RecyclerView

    var currentSkillTabIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayerCareerBinding.inflate(layoutInflater)

        skillPerformanceRecyclerView = binding.skillPerformanceRecyclerView
        skillPerformanceRecyclerView.setHasFixedSize(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(args.flagImagePath).fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .error(R.drawable.ic_connection_error)
            .into(binding.coverImage)
        Glide.with(requireContext()).load(args.playerImagePath).centerInside()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .priority(Priority.HIGH)
            .error(R.drawable.ic_connection_error)
            .into(binding.profileImage)

        binding.playerNameTxtV.text = args.playerName


        selectSkillTab()

        initialiseLayoutManager()

        binding.battingTab.setOnClickListener{
            currentSkillTabIndex = 1
            selectSkillTab()
            updateRecyclerView()
        }

        binding.bowlingTab.setOnClickListener{
            currentSkillTabIndex = 2
            selectSkillTab()
            updateRecyclerView()
        }
    }
    private fun initialiseLayoutManager() {
        skillPerformanceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        updateRecyclerView()
    }

    private fun observeData() {
        viewModel.getCareerByPlayerId(args.playerID)

        viewModel.playerCareer.observe(viewLifecycleOwner){
            binding.birthdayTxtV.text = "BORN ${it.data?.dateofbirth}"
            binding.countryTxtV.text = args.playerCountryName
            binding.positionValueTxtV.text = it.data?.position?.name
            binding.batiingStyleValueTxtV.text = it.data?.battingstyle.toString()
            binding.bowlingStyleValueTxtV.text = it.data?.bowlingstyle.toString()

            var careerList = it.data?.career?.sortedByDescending { it.updated_at }

            if(careerList!=null && careerList.isNotEmpty()){
                if(currentSkillTabIndex==1){
                    skillPerformanceRecyclerView.adapter =
                        it.data?.let { it1 -> BattingCareerAdapter(requireContext(), viewModel, it1, careerList) }
                }else{
                    skillPerformanceRecyclerView.adapter =
                        it.data?.let { it1 -> BowlingCareerAdapter(requireContext(), viewModel, it1, careerList) }
                }
            }

        }
    }

    fun updateRecyclerView(){
        observeData()
    }

    fun selectSkillTab(){

        when(currentSkillTabIndex){
            1 -> {
                binding.skillColumn.text = "Batting"
                binding.battingTab.setBackgroundResource(R.color.colorSelectedTab)
                binding.battingTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTabIndicator))

                binding.bowlingTab.setBackgroundResource(R.drawable.tab_border_shape)
                binding.bowlingTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))
            }
            2 -> {
                binding.skillColumn.text = "Bowling"
                binding.bowlingTab.setBackgroundResource(R.color.colorSelectedTab)
                binding.bowlingTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorTabIndicator))

                binding.battingTab.setBackgroundResource(R.drawable.tab_border_shape)
                binding.battingTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnBackground))
            }
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

}