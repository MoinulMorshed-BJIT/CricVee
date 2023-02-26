package com.moinul.cricvee.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moinul.cricvee.R
import com.moinul.cricvee.adapter.StatOptionsAdapter
import com.moinul.cricvee.utils.UtilTools

class StatisticsFragment : Fragment() {

    private var columnCount = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics_list, container, false)
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = StatOptionsAdapter(UtilTools.allOptions)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottom_navbar)
        bottomNavigationView?.let {
            it.menu.getItem(3).isChecked = true
        }
    }
}