package com.timurkhabibulin.myhabits.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurkhabibulin.myhabits.HabitsAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.activities.EditingActivityMode
import com.timurkhabibulin.myhabits.activities.HabitEditingActivity
import com.timurkhabibulin.myhabits.activities.ITEM_POSITION_PARAM
import com.timurkhabibulin.myhabits.habitModel.Habit
import com.timurkhabibulin.myhabits.habitModel.HabitService
import com.timurkhabibulin.myhabits.habitModel.HabitsListener
import kotlinx.android.synthetic.main.fragment_habit_list.*

enum class HabitListDisplayMode {
    GOOD_HABITS, BAD_HABITS
}

class HabitListFragment : Fragment() {

    private lateinit var displayMode: HabitListDisplayMode
    private lateinit var habitAdapter: HabitsAdapter

    private val habitsService: HabitService
        get() = HabitService

    private val habitsListener: HabitsListener = { habitAdapter.habits = it }

    companion object {

        @JvmStatic
        fun newInstance(displayMode: HabitListDisplayMode) =
            HabitListFragment().apply {
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
        updateHabitList()
    }

    override fun onDestroy() {
        super.onDestroy()
        habitsService.removeListener(habitsListener)
    }

    private fun setUpHabitList() {
        habitAdapter = HabitsAdapter(displayMode) { itemPosition ->
            openHabitEditingActivity(itemPosition)
        }

        habitsService.addListener(habitsListener)
        val manager = LinearLayoutManager(activity)

        recycler_view.adapter = habitAdapter
        recycler_view.layoutManager = manager
    }

    private fun openHabitEditingActivity(itemPosition: Int) {
        val sendIntent = Intent(activity, HabitEditingActivity::class.java).apply {
            val bundle = Bundle().apply {
                putString(
                    EditingActivityMode::class.java.toString(),
                    EditingActivityMode.EDIT.toString()
                )
                putInt(ITEM_POSITION_PARAM, itemPosition)
            }
            putExtras(bundle)
        }
        startActivity(sendIntent)
    }

    private fun updateHabitList() {
        mutableListOf<Habit>().addAll(habitsService.getAllHabits())

        habitAdapter.submitList(habitsService.getAllHabits().filter {
            when (displayMode) {
                HabitListDisplayMode.GOOD_HABITS -> it.type == getString(R.string.good_habit_type)
                HabitListDisplayMode.BAD_HABITS -> it.type == getString(R.string.bad_habit_type)
            }
        })
    }

}