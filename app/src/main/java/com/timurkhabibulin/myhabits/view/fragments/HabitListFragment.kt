package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.view.HabitsAdapter
import com.timurkhabibulin.myhabits.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_habit_list.*

//const val HABIT_LIST_FRAGMENT = "HabitListFragment"

class HabitListFragment : Fragment() {

    private var fragmentMode = HabitType.GOOD

    private lateinit var habitAdapter: HabitsAdapter

    private lateinit var viewModel: HabitListViewModel

    companion object {

        @JvmStatic
        fun newInstance(displayMode: HabitType) =
            HabitListFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        HabitType::class.java.toString(),
                        displayMode.toString()
                    )
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(HabitType::class.java.toString()) ?: ""
            fragmentMode = HabitType.valueOf(actModeStr)
        }

        viewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHabitList()
    }

    private fun setUpHabitList() {
        val manager = LinearLayoutManager(activity)

        habitAdapter = HabitsAdapter { itemID ->
            openHabitEditingFragment(itemID)
        }
        viewModel.addObservers(fragmentMode, viewLifecycleOwner, ::onHabitsChanged)

        recycler_view.adapter = habitAdapter
        recycler_view.layoutManager = manager
    }

    private fun onHabitsChanged(habits: List<Habit>?) {
        habitAdapter.submitList(habits)
    }

    private fun openHabitEditingFragment(itemID: Int) {
        val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.EDIT.toString(), itemID)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.rootFragment, fragment, HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }
}