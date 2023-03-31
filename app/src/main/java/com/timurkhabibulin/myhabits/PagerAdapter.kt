package com.timurkhabibulin.myhabits

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.timurkhabibulin.myhabits.fragments.HabitListFragment
import com.timurkhabibulin.myhabits.habitModel.HabitType

class PagerAdapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> HabitListFragment.newInstance(HabitType.GOOD)
            else -> HabitListFragment.newInstance(HabitType.BAD)
        }
}