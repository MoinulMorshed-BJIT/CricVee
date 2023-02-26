package com.moinul.cricvee.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.LineupsAdapter
import com.moinul.cricvee.databinding.FragmentLineupsBinding
import com.moinul.cricvee.utils.Constants
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "LineupsFragment"

class LineupsFragment : Fragment() {
    private lateinit var parentMatchDetailsFragment: MatchDetailsFragment
    private lateinit var binding: FragmentLineupsBinding
    private val viewModel: SportsViewModel by viewModels()
    private lateinit var teamXIRecyclerViewA: RecyclerView
    private lateinit var teamBenchRecyclerViewA: RecyclerView
    private lateinit var teamXIRecyclerViewB: RecyclerView
    private lateinit var teamBenchRecyclerViewB: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLineupsBinding.inflate(layoutInflater)

        if (savedInstanceState != null) {
            binding.localTeamHeader.text =
                savedInstanceState.getString(Constants.LOCAL_TEAM_TEXT_VIEW_VALUE_KEY)
            binding.visitorTeamHeader.text =
                savedInstanceState.getString(Constants.VISITOR_TEAM_TEXT_VIEW_VALUE_KEY)
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
        showProgressBar()
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
        viewModel.fixtureWithScoreboard.observe(viewLifecycleOwner) {
            val visitorTeamId = it.data?.visitorteam_id
            val localTeamId = it.data?.localteam_id
            GlobalScope.launch(Dispatchers.IO) {
                val team = visitorTeamId?.let { it1 -> viewModel.readTeamById(it1) }
                GlobalScope.launch(Dispatchers.Main) {
                    binding.visitorTeamHeader.text = team?.code
                    Glide.with(requireContext()).load(team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error).into(binding.visitorImgSmall)
                }
            }

            GlobalScope.launch(Dispatchers.IO) {
                val team = localTeamId?.let { it1 -> viewModel.readTeamById(it1) }
                GlobalScope.launch(Dispatchers.Main) {
                    binding.localTeamHeader.text = team?.code
                    Glide.with(requireContext()).load(team?.image_path).fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.HIGH)
                        .error(R.drawable.ic_connection_error).into(binding.localImgSmall)
                }
            }

            val teamA = it.data?.lineup?.filter { it.lineup?.team_id == visitorTeamId }
            val teamB = it.data?.lineup?.filter { it.lineup?.team_id == localTeamId }

            val teamAXI = teamA?.filter { it.lineup?.substitution == false }
            val benchTeamA = teamA?.filter { it.lineup?.substitution == true }

            val teamBXI = teamB?.filter { it.lineup?.substitution == false }
            val benchTeamB = teamB?.filter { it.lineup?.substitution == true }

            teamXIRecyclerViewA.adapter =
                teamAXI?.let { it1 -> LineupsAdapter(requireContext(), viewModel, it1, 1) }
            teamXIRecyclerViewB.adapter =
                teamBXI?.let { it1 -> LineupsAdapter(requireContext(), viewModel, it1, 2) }
            teamBenchRecyclerViewA.adapter =
                benchTeamA?.let { it1 -> LineupsAdapter(requireContext(), viewModel, it1, 1) }
            teamBenchRecyclerViewB.adapter =
                benchTeamB?.let { it1 -> LineupsAdapter(requireContext(), viewModel, it1, 2) }
            hideProgressBar()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentMatchDetailsFragment = requireParentFragment() as MatchDetailsFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            Constants.VISITOR_TEAM_TEXT_VIEW_VALUE_KEY, binding.visitorTeamHeader.text.toString()
        )
        outState.putString(
            Constants.LOCAL_TEAM_TEXT_VIEW_VALUE_KEY, binding.localTeamHeader.text.toString()
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            binding.localTeamHeader.text =
                savedInstanceState.getString(Constants.LOCAL_TEAM_TEXT_VIEW_VALUE_KEY)
            binding.visitorTeamHeader.text =
                savedInstanceState.getString(Constants.VISITOR_TEAM_TEXT_VIEW_VALUE_KEY)
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}