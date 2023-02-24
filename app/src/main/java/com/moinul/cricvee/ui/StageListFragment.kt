package com.moinul.cricvee.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.FixtureOptionsAdapter
import com.moinul.cricvee.adapter.StageOptionsAdapter
import com.moinul.cricvee.utils.UtilTools
import com.moinul.cricvee.viewmodel.SportsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class StageListFragment : Fragment() {
    private var columnCount = 1
    private val viewModel: SportsViewModel by viewModels()
    private val args: StageListFragmentArgs by navArgs()
    var leagueId by Delegates.notNull<Int>()
    var seasonId by Delegates.notNull<Int>()
    var leagueImagePath by Delegates.notNull<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stage_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            leagueId = args.leagueId
            seasonId = args.seasonId
            leagueImagePath = args.leagueImagePath
            viewModel.readStagesByLeagueId(leagueId, seasonId).observe(viewLifecycleOwner){
                if(it!=null && it.isNotEmpty()){
                    var stageList = mutableListOf<UtilTools.Companion.Option>()
                    for(stage in it) {
                        stageList.add(UtilTools.Companion.Option(stage.id, stage.name.toString(),
                            leagueImagePath, args.leagueName))
                    }
                    with(view) {
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                        adapter = StageOptionsAdapter(requireContext(), stageList)
                    }
                }
            }


        }
        return view
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
