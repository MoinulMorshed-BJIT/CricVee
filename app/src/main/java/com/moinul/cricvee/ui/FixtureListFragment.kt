package com.moinul.cricvee.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.FixtureOptionsAdapter
import com.moinul.cricvee.adapter.StatOptionsAdapter
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel

class FixtureListFragment : Fragment() {
    private var columnCount = 1
    private val viewModel: SportsViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fixture_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {

            viewModel.readAllLeagueData.observe(viewLifecycleOwner){
                if(it!=null && it.isNotEmpty()){
                    var leagueList = mutableListOf<UtilTools.Companion.Option>()
                    for(league in it){
                        league.season_id?.let { it1 ->
                            UtilTools.Companion.Option(league.name.toString(), league.image_path.toString(), league.id,
                                it1
                            )
                        }?.let { it2 -> leagueList.add(it2) }
                    }
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = FixtureOptionsAdapter(requireContext(), leagueList)
                    }
                }
            }


        }
        return view
    }
    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView?.let {
            it.menu.getItem(1).isChecked = true
        }
    }


}