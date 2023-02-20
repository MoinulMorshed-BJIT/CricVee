package com.moinul.cricvee.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.LineupsAdapter

import com.moinul.cricvee.databinding.FragmentLineupsBinding
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel

private const val TAG = "LineupsFragment"

class LineupsFragment : Fragment() {
    private lateinit var parentMatchDetailsFragment: MatchDetailsFragment
    private lateinit var binding:FragmentLineupsBinding
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var teamXIRecyclerViewA: RecyclerView
    private lateinit var teamBenchRecyclerViewA: RecyclerView
    private lateinit var teamXIRecyclerViewB: RecyclerView
    private lateinit var teamBenchRecyclerViewB: RecyclerView
   /* private var visitorTeamId:Int = 0
    private var localTeamId:Int = 0*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLineupsBinding.inflate(layoutInflater)

        if (savedInstanceState != null) {
            binding.localTeamHeader.text = savedInstanceState.getString("localTeamTextViewValue")
            binding.visitorTeamHeader.text = savedInstanceState.getString("visitorTeamTextViewValue")
        }else{
            binding.visitorTeamHeader.text = parentMatchDetailsFragment.view?.findViewById<TextView>(R.id.visitor_team_name)?.text.toString()
            binding.localTeamHeader.text = parentMatchDetailsFragment.view?.findViewById<TextView>(R.id.local_team_name)?.text.toString()


        }

        teamXIRecyclerViewA = binding.xiARecyclerView
        teamXIRecyclerViewA.setHasFixedSize(true)

        teamXIRecyclerViewB = binding.xiBRecyclerView
        teamXIRecyclerViewB.setHasFixedSize(true)

        teamBenchRecyclerViewA = binding.benchARecyclerView
        teamBenchRecyclerViewA.setHasFixedSize(true)

        teamBenchRecyclerViewB = binding.benchBRecyclerView
        teamBenchRecyclerViewB.setHasFixedSize(true)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseLayoutManager()
    }
    private fun initialiseLayoutManager() {
        teamXIRecyclerViewA.layoutManager = LinearLayoutManager(requireContext())
        teamXIRecyclerViewB.layoutManager = LinearLayoutManager(requireContext())
        teamBenchRecyclerViewA.layoutManager = LinearLayoutManager(requireContext())
        teamBenchRecyclerViewB.layoutManager = LinearLayoutManager(requireContext())
        observeData()
    }

    private fun observeData() {

        viewModel.getFixtureWithScoreboard(UtilTools.CLICKED_FIXTURE_ID)
        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner){
            val visitorTeamId = it.data?.visitorteam_id
            val localTeamId = it.data?.localteam_id
            var localTeamFound = false
            var visitorTeamFound = false
            /*try {
                for(run in it.data?.runs!!){
                    if(run.team_id==localTeamId){
                        localTotalValue = "Total: ${run.score}/${run.wickets} (${run.overs})"
                    }else{
                        visitorTotalValue = "Total: ${run.score}/${run.wickets} (${run.overs})"
                    }
                }
            }catch (e: Exception){
                Log.d(TAG, "onViewCreated: $e")
            }*/

            for(teams in it.data?.balls!!){
                if(localTeamFound && visitorTeamFound){
                    break;
                }
                if(!localTeamFound && teams.team?.id == localTeamId){
                    binding.localTeamHeader.text = teams.team?.code
                    Glide.with(requireContext()).load(teams.team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.localImgSmall)
                    localTeamFound = true
                }

                else if(!visitorTeamFound && teams.team?.id == visitorTeamId){
                    binding.visitorTeamHeader.text = teams.team?.code
                    Glide.with(requireContext()).load(teams.team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error)
                        .into(binding.visitorImgSmall)
                    visitorTeamFound = true
                }
            }

            val teamA = it.data?.lineup?.filter { it.lineup?.team_id==visitorTeamId }
            val teamB = it.data?.lineup?.filter { it.lineup?.team_id==localTeamId }

            val teamAXI = teamA?.filter { it.lineup?.substitution==false }
            val benchTeamA = teamA?.filter { it.lineup?.substitution==true }

            val teamBXI = teamB?.filter { it.lineup?.substitution==false }
            val benchTeamB = teamB?.filter { it.lineup?.substitution==true }

            teamXIRecyclerViewA.adapter =
                teamAXI?.let { it1 -> LineupsAdapter(requireContext(), viewModel, it1, 1) }
            teamXIRecyclerViewB.adapter =
                teamBXI?.let { it1 -> LineupsAdapter(requireContext(),viewModel, it1, 2) }
            teamBenchRecyclerViewA.adapter =
                benchTeamA?.let { it1 -> LineupsAdapter(requireContext(),viewModel, it1, 1) }
            teamBenchRecyclerViewB.adapter =
                benchTeamB?.let { it1 -> LineupsAdapter(requireContext(),viewModel, it1, 2) }



        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentMatchDetailsFragment = requireParentFragment() as MatchDetailsFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("visitorTeamTextViewValue", binding.visitorTeamHeader.text.toString())
        outState.putString("localTeamTextViewValue", binding.localTeamHeader.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.localTeamHeader.text = savedInstanceState.getString("localTeamTextViewValue")
            binding.visitorTeamHeader.text = savedInstanceState.getString("visitorTeamTextViewValue")
        }
    }


}