package com.timurkhabibulin.myhabits.presentation.view

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.timurkhabibulin.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.presentation.view.fragments.HabitListFragment

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