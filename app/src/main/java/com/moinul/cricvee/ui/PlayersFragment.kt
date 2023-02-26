package com.moinul.cricvee.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.PlayerSearchAdapter
import com.moinul.cricvee.databinding.FragmentPlayersBinding
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.fragment_players.*


class PlayersFragment : Fragment() {
    private lateinit var binding: FragmentPlayersBinding
    private lateinit var searchPlayerRecyclerView: RecyclerView
    private val viewModel: SportsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        searchPlayerRecyclerView = binding.searchPlayerRecyclerView
        searchPlayerRecyclerView.setHasFixedSize(true)

        viewModel.readAllTeamIdList.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                viewModel.fetchCurrentSquad(it)
            }
        }
        initialiseLayoutManager()

        Log.d("Fragment e Search Test", "onCreateOptionsMenu: HERE!!")
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(newText: String?): Boolean {
                binding.searchView.clearFocus()
                val adapter = searchPlayerRecyclerView.adapter as PlayerSearchAdapter?
                Log.d("Fragment e Search Test", "onQueryTextSubmit: $newText")
                if (newText != null) {
                    adapter?.performSearch(newText)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val adapter = searchPlayerRecyclerView.adapter as PlayerSearchAdapter?
                Log.d("Fragment e Search Test", "onQueryTextSubmit: $newText")
                if (newText != null) {
                    adapter?.performSearch(newText)
                }
                return false
            }
        })

    }

    private fun initialiseLayoutManager() {
        searchPlayerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        observeData()
    }

    private fun observeData() {
        viewModel.readAllSquadPlayers.observe(viewLifecycleOwner) {
            val adapterScrollState = search_player_recyclerView.layoutManager?.onSaveInstanceState()
            searchPlayerRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            searchPlayerRecyclerView.adapter = PlayerSearchAdapter(requireContext(), viewModel, it)
            Log.d("Observe first", "observeData: Here first before Search")
            hideProgressBar()
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView?.let {
            it.menu.getItem(2).isChecked = true
        }
        binding.searchView.isIconified = true
        binding.searchView.clearFocus()
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

}