package com.moinul.cricvee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.MatchAdapter
import com.moinul.cricvee.databinding.FragmentTournamentFixtureBinding
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class TournamentFixtureFragment : Fragment() {
    private lateinit var binding: FragmentTournamentFixtureBinding
    private val viewModel: SportsViewModel by viewModels()
    private val args: TournamentFixtureFragmentArgs by navArgs()
    private lateinit var tournamentFixtureRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTournamentFixtureBinding.inflate(layoutInflater)
        tournamentFixtureRecyclerView = binding.tournamentFixtureRecyclerview
        tournamentFixtureRecyclerView.setHasFixedSize(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        initialiseLayoutManager()
    }

    fun initialiseLayoutManager() {
        tournamentFixtureRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeData()
    }

    fun observeData() {
        viewModel.readFixturesByStageId(args.stageId).observe(viewLifecycleOwner) {
            val adapterScrollState =
                tournamentFixtureRecyclerView.layoutManager?.onSaveInstanceState()
            tournamentFixtureRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            tournamentFixtureRecyclerView.adapter =
                MatchAdapter(requireContext(), viewModel, it, viewLifecycleOwner, true)
            hideProgressBar()
        }

        Glide.with(requireContext()).load(args.leagueImagePath).fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.HIGH)
            .error(R.drawable.ic_connection_error).into(binding.leagueImageView)
        binding.fixtureLeagueTitle.text = args.leagueName + "\nFixture"

    }

    override fun onResume() {
        super.onResume()
        requireActivity().bottom_navbar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}