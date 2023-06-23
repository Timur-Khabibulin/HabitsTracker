package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.databinding.FragmentHomeBinding
import com.timurkhabibulin.myhabits.presentation.view.PagerAdapter

const val HOME_FRAGMENT_NAME = "HomeFragment"

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        binding.makeNewHabitBtn.setOnClickListener {
            openHabitEditingFragment()
        }

        val names = listOf(getString(R.string.good_habits), getString(R.string.bad_habits))

        this.let { fragment ->
            binding.viewPager.adapter = PagerAdapter(fragment)
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = names[position]
            }.attach()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openHabitEditingFragment() {
        val action = MenuFragmentDirections
            .actionMenuFragment3ToHabitEditingFragment2(EditingFragmentMode.ADD.toString(), -1)
        findNavController().navigate(action)
    }

}