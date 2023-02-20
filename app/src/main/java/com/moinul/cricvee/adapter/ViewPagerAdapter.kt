package com.moinul.cricvee.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.moinul.cricvee.ui.BattingScoreboardFragment
import com.moinul.cricvee.ui.LineupsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    private val childFragmentManager = fragmentManager


    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> BattingScoreboardFragment()
            1 -> LineupsFragment()

            else -> Fragment()
        }
    }
}