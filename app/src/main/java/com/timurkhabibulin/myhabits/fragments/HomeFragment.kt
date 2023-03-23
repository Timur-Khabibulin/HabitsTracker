package com.timurkhabibulin.myhabits.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.timurkhabibulin.myhabits.PagerAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.activities.EditingActivityMode
import com.timurkhabibulin.myhabits.activities.HabitEditingActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var displayMode: HabitListDisplayMode

    companion object {

        @JvmStatic
        fun newInstance(displayMode: HabitListDisplayMode) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        HabitListDisplayMode::class.java.toString(),
                        displayMode.toString()
                    )
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(HabitListDisplayMode::class.java.toString()) ?: ""
            displayMode = HabitListDisplayMode.valueOf(actModeStr)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        make_new_habit.setOnClickListener {
            openHabitEditingActivity()
        }

        val names = listOf(getString(R.string.good_habits), getString(R.string.bad_habits))
        activity?.let { activity ->
            viewPager.adapter = PagerAdapter(activity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = names[position]
            }.attach()
        }
    }

    private fun openHabitEditingActivity() {
        val sendIntent = Intent(activity, HabitEditingActivity::class.java).apply {
            val bundle = Bundle().apply {
                putString(
                    EditingActivityMode::class.java.toString(),
                    EditingActivityMode.ADD.toString()
                )
               // putInt(ITEM_POSITION_PARAM, itemPosition)
            }
            putExtras(bundle)
        }
        startActivity(sendIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}