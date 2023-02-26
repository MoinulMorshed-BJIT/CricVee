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
import kotlinx.android.synthetic.main.fragment_match_details.*
import kotlinx.android.synthetic.main.fragment_match_details.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        showProgressBar()


        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager2
        viewPager2.adapter = viewPagerAdapter
        viewPager2.isUserInputEnabled = true


        viewModel.getFixtureWithScoreboard(UtilTools.CLICKED_FIXTURE_ID)

        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner){
            val visitorTeamId = it.data?.visitorteam_id
            val localTeamId = it.data?.localteam_id
            var localTotalValue = ""
            var visitorTotalValue = ""
            try {
                for(run in it.data?.runs!!){
                    if(run.team_id==localTeamId){
                        localTotalValue = "Total: ${run.score}/${run.wickets} (${run.overs})"
                    }else{
                        visitorTotalValue = "Total: ${run.score}/${run.wickets} (${run.overs})"
                    }
                }
            }catch (e: Exception){
                Log.d(TAG, "onViewCreated: $e")
            }

            binding.localTotal.text = localTotalValue
            binding.visitorTotal.text = visitorTotalValue

            GlobalScope.launch(Dispatchers.IO){
                val team = visitorTeamId?.let { it1 -> viewModel.readTeamById(it1) }
                GlobalScope.launch(Dispatchers.Main){
                    binding.visitorTeamName.text = team?.name
                    Glide.with(requireContext()).load(team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.visitorTeamImg)
                }
            }

            GlobalScope.launch(Dispatchers.IO) {
                val team = localTeamId?.let { it1 -> viewModel.readTeamById(it1) }
                GlobalScope.launch(Dispatchers.Main) {
                    binding.localTeamName.text = team?.name
                    Glide.with(requireContext()).load(team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.localTeamImg)
                }
            }
            hideProgressBar()
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "MATCH INFO"
                }
                1 -> {
                    tab.text = "SCORECARD"
                }
                2 -> {
                    tab.text = "LINEUP"
                }


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
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}
