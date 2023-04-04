package com.timurkhabibulin.myhabits.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurkhabibulin.myhabits.view.HabitsAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitService
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.model.HabitsListener
import com.timurkhabibulin.myhabits.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_habit_list.*

const val HABIT_LIST_FRAGMENT = "HabitListFragment"

class HabitListFragment : Fragment() {

    private var displayMode = HabitType.GOOD
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        habitAdapter = HabitsAdapter { itemID ->
            openHabitEditingFragment(itemID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(HabitType::class.java.toString()) ?: ""
            displayMode = HabitType.valueOf(actModeStr)
        }

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitListViewModel(HabitService, displayMode) as T
            }
        })[HabitListViewModel::class.java]
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


    override fun onStart() {
        super.onStart()
        viewModel.habits.observe(viewLifecycleOwner, ::onHabitsLoaded)
    }

    fun onHabitsLoaded(habits: MutableList<Habit>) {
        habitAdapter.habits = habits
        habitAdapter.submitList(habits)
    }

    private fun setUpHabitList() {
        val manager = LinearLayoutManager(activity)

        recycler_view.adapter = habitAdapter
        recycler_view.layoutManager = manager
    }

    private fun openHabitEditingFragment(itemID: Int) {
        val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.EDIT.toString(), itemID)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.contentFrame, fragment, HABIT_EDITING_FRAGMENT_NAME)
           // ?.addToBackStack(HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }

}