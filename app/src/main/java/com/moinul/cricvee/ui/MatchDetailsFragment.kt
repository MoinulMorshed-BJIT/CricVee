package com.moinul.cricvee.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.tabs.TabLayoutMediator
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.ViewPagerAdapter
import com.moinul.cricvee.databinding.FragmentMatchDetailsBinding
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_match_details.view.*

private const val TAG = "MatchDetailsFragment"
class MatchDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMatchDetailsBinding
    private val viewModel: SportsViewModel by viewModels()
    private val args: MatchDetailsFragmentArgs by navArgs()
    private var fixtureId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            fixtureId = savedInstanceState.getInt("fixtureId", 0)
        } else {
            // Initialize idValue with the argument value
            fixtureId = args.fixtureId
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("fixtureId", fixtureId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        try{
            binding = FragmentMatchDetailsBinding.inflate(layoutInflater)    
        }catch (e:Exception){
            Log.d(TAG, "onCreateView: $e")
        }
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().bottom_navbar.visibility=View.GONE
        /*val bundle = Bundle()
        bundle.putInt("fixtureId", args.fixtureId)*/

        /*val childBattingScoreboardFragment = BattingScoreboardFragment()
        childBattingScoreboardFragment.arguments = bundle*/

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager2
        viewPager2.adapter = viewPagerAdapter
        viewPager2.isUserInputEnabled = true



        viewModel.getFixtureWithScoreboard(UtilTools.CLICKED_FIXTURE_ID)

        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner){
            val visitorTeamId = it.data?.visitorteam_id
            val localTeamId = it.data?.localteam_id
            var localTeamFound = false
            var visitorTeamFound = false
            for(teams in it.data?.balls!!){
                if(localTeamFound && visitorTeamFound){
                    break;
                }
                if(!localTeamFound && teams.team?.id == localTeamId){
                    binding.localTeamName.text = teams.team?.name
                    Glide.with(requireContext()).load(teams.team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.localTeamImg)
                    localTeamFound = true
                }

                else if(!visitorTeamFound && teams.team?.id == visitorTeamId){
                    binding.visitorTeamName.text = teams.team?.name
                    Glide.with(requireContext()).load(teams.team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.visitorTeamImg)
                    visitorTeamFound = true
                }
            }

        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "BATTING"
                    tab.setIcon(R.drawable.icons_cricket_player)
                }
               /* 1 -> {
                    tab.text = "Business"
                    tab.setIcon(R.drawable.icon_business_news)
                }
                2 -> {
                    tab.text = "Entertainment"
                    tab.setIcon(R.drawable.icon_entertainment)

                }
                3 -> {
                    tab.text = "General"
                    tab.setIcon(R.drawable.icon_general)
                }
                4 -> {
                    tab.text = "Health"
                    tab.setIcon(R.drawable.icon_health)
                }
                5 -> {
                    tab.text = "Science"
                    tab.setIcon(R.drawable.icon_science)
                }
                6 -> {
                    tab.text = "Sports"
                    tab.setIcon(R.drawable.icon_sports)
                }
                7 -> {
                    tab.text = "Technology"
                    tab.setIcon(R.drawable.icon_technology)
                }*/

            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().bottom_navbar.visibility=View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity().bottom_navbar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        UtilTools.CLICKED_FIXTURE_ID = 0
    }

}
