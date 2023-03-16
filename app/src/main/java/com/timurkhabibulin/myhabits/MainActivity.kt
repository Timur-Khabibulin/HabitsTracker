package com.timurkhabibulin.myhabits

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.DynamicColors
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var habitAdapter: HabitsAdapter

    private val habitsService: HabitService
        get() = HabitService

    private val habitsListener: HabitsListener = { habitAdapter.habits = it }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         DynamicColors.applyToActivityIfAvailable(this)

        make_new_habit.setOnClickListener {
            openHabitEditingActivity(EditingActivityMode.ADD, -1)
        }
    }

    override fun onStart() {
        super.onStart()
        updateList()
    }

    private fun openHabitEditingActivity(mode: EditingActivityMode, itemPosition: Int) {
        val sendIntent = Intent(this, HabitEditingActivity::class.java).apply {
            val bundle = Bundle().apply {
                putString(EditingActivityMode::class.java.toString(), mode.toString())
                putInt("itemPosition", itemPosition)
            }
            putExtras(bundle)
        }
        startActivity(sendIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        habitsService.removeListener(habitsListener)
    }

    private fun updateList() {
        habitAdapter = HabitsAdapter { itemPosition ->
            openHabitEditingActivity(EditingActivityMode.EDIT, itemPosition)
        }

        habitsService.addListener(habitsListener)
        val manager = LinearLayoutManager(this)

        recycler_view.adapter = habitAdapter
        recycler_view.layoutManager = manager
        habitAdapter.submitList(habitsService.getAllHabits())
    }
}