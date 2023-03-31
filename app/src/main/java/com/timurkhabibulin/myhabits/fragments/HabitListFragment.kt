package com.timurkhabibulin.myhabits.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurkhabibulin.myhabits.HabitsAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.habitModel.HabitService
import com.timurkhabibulin.myhabits.habitModel.HabitType
import com.timurkhabibulin.myhabits.habitModel.HabitsListener
import kotlinx.android.synthetic.main.fragment_habit_list.*

const val HABIT_LIST_FRAGMENT = "HabitListFragment"

class HabitListFragment : Fragment() {

    private var displayMode = HabitType.GOOD
    private lateinit var habitAdapter: HabitsAdapter

    private val habitsService: HabitService
        get() = HabitService

    private val habitsListener: HabitsListener = { habitAdapter.habits = it }

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
        habitsListener.invoke(habitAdapter.habits)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        habitsService.removeListener(habitsListener)
    }

    private fun setUpHabitList() {
        habitsService.addListener(habitsListener, displayMode)
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
            ?.addToBackStack(HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }

}