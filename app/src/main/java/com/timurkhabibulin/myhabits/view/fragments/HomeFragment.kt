package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.timurkhabibulin.myhabits.view.PagerAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.HabitType
import kotlinx.android.synthetic.main.fragment_habit_filter.*
import kotlinx.android.synthetic.main.fragment_home.*

const val HOME_FRAGMENT_NAME = "HomeFragment"
const val HABIT_TYPE_PARAM = "HabitType"

class HomeFragment : Fragment() {
    private lateinit var displayMode: HabitType


    companion object {

        @JvmStatic
        fun newInstance(displayMode: HabitType) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        HABIT_TYPE_PARAM,
                        displayMode.toString()
                    )
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(HABIT_TYPE_PARAM) ?: ""
            displayMode = HabitType.valueOf(actModeStr)
        }
//        bottomSheetBehavior = BottomSheetBehavior.from(habitFilter)
        // bottomSheetBehavior.state = Botto
        /*  if (savedInstanceState == null) {
              parentFragmentManager
                  .beginTransaction()
                  .add(R.id.bottomSheet, HabitFilterFragment.newInstance(), tag)
                  .setReorderingAllowed(true)
                  .commit()
          }*/
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

        childFragmentManager.beginTransaction()
            .add(R.id.bottomSheet, HabitFilterFragment.newInstance())
            .commit()

        make_new_habit.setOnClickListener {
            openHabitEditingFragment()
        }

        val names = listOf(getString(R.string.good_habits), getString(R.string.bad_habits))
        activity?.let { activity ->
            viewPager.adapter = PagerAdapter(activity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = names[position]
            }.attach()
        }

    }

    private fun openHabitEditingFragment() {
        val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.ADD.toString(), -1)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.contentFrame, fragment, HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }
}