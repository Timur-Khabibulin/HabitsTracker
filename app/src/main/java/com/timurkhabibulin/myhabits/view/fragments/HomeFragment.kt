package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.HabitService
import com.timurkhabibulin.myhabits.view.PagerAdapter
import com.timurkhabibulin.myhabits.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_home.*

const val HOME_FRAGMENT_NAME = "HomeFragment"

class HomeFragment : Fragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createPagerFragmentViewModel()
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
        activity?.let { activity ->
            viewPager.adapter = PagerAdapter(activity)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = names[position]
            }.attach()
        }

    }

    private fun createPagerFragmentViewModel() {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitListViewModel(HabitService) as T
            }
        })[HabitListViewModel::class.java]
    }

    private fun openHabitEditingFragment() {
        val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.ADD.toString(), -1)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.contentFrame, fragment, HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }
}