package com.timurkhabibulin.myhabits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.timurkhabibulin.myhabits.PagerAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.habitModel.HabitType
import kotlinx.android.synthetic.main.fragment_home.*

const val HOME_FRAGMENT_NAME = "HomeFragment"
const val HABIT_TYPE_PARAM="HabitType"

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

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
       /* findNavController().navigate(
            R.id.habitEditingFragment,
            Bundle().apply {
                putString(EDITING_FRAGMENT_MODE_PARAM, EditingFragmentMode.ADD.toString())
                putInt(ITEM_ID_PARAM, -1)
            }
        )*/

         val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.ADD.toString(), -1)
          activity?.supportFragmentManager
              ?.beginTransaction()
              ?.replace(R.id.contentFrame, fragment, HABIT_EDITING_FRAGMENT_NAME)
              ?.addToBackStack(HABIT_EDITING_FRAGMENT_NAME)
              ?.commit()
    }
}