package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.view.PagerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

const val HOME_FRAGMENT_NAME = "HomeFragment"

class HomeFragment : Fragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    companion object {

        @JvmStatic
        fun newInstance():HomeFragment {
           return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        make_new_habit.setOnClickListener {
            openHabitEditingFragment()
        }

        val names = listOf(getString(R.string.good_habits), getString(R.string.bad_habits))

        this.let { fragment->
            viewPager.adapter = PagerAdapter(fragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = names[position]
            }.attach()
        }

    }

    private fun openHabitEditingFragment() {
        val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.ADD.toString(), -1)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.rootFragment, fragment, HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }

}