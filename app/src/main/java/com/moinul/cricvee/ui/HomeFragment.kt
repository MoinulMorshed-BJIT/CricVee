package com.moinul.cricvee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.MatchAdapter
import com.moinul.cricvee.databinding.FragmentHomeBinding
import com.moinul.cricvee.viewmodel.SportsViewModel

class HomeFragment : Fragment() {
    private lateinit var recentMatchRecyclerView: RecyclerView
    private val viewModel: SportsViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        //binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recentMatchRecyclerView = binding.recentMatchRecyclerView
        recentMatchRecyclerView.setHasFixedSize(true)
        initialiseLayoutManager()
    }

    fun initialiseLayoutManager(){
        recentMatchRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        observeData()
    }

    fun observeData(){
        viewModel.readRecentFixtureData.observe(viewLifecycleOwner){
            val adapterScrollState = recentMatchRecyclerView.layoutManager?.onSaveInstanceState()
            recentMatchRecyclerView.layoutManager?.onRestoreInstanceState(adapterScrollState)
            recentMatchRecyclerView.adapter = MatchAdapter(requireContext(), viewModel, it)
        }
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView?.let {
            it.menu.getItem(0).isChecked = true
        }
    }

}