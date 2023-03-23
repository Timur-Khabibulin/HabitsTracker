package com.timurkhabibulin.myhabits

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.timurkhabibulin.myhabits.fragments.HabitListDisplayMode
import com.timurkhabibulin.myhabits.fragments.HabitListFragment

class PagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> HabitListFragment.newInstance(HabitListDisplayMode.GOOD_HABITS)
            else -> HabitListFragment.newInstance(HabitListDisplayMode.BAD_HABITS)
        }
}