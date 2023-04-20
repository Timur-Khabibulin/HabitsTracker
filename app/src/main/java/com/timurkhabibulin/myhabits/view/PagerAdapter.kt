package com.timurkhabibulin.myhabits.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.view.fragments.HabitListFragment

class PagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
       return when (position) {
            0 -> HabitListFragment.newInstance(HabitType.GOOD)
            else -> HabitListFragment.newInstance(HabitType.BAD)
        }
    }

}