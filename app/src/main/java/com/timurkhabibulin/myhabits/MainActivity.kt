package com.timurkhabibulin.myhabits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.habit_item.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var habitAdapter: HabitsAdapter

    private val habitsService: HabitService
        get() = HabitService

    private val habitsListener: HabitsListener = { habitAdapter.habits = it }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateList()

        make_new_habit.setOnClickListener() {
            openHabitEditingActivity("add")
        }
    }

    private fun openHabitEditingActivity(type: String) {
        val sendIntent = Intent(this, HabitEditingActivity::class.java).apply {
            val bundle = Bundle().apply {
                putString("type", type)
            }
            putExtras(bundle)
        }
        startActivity(sendIntent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        habitsService.removeListener(habitsListener)
    }

    private fun updateList() {
        habitAdapter = HabitsAdapter() {
            openHabitEditingActivity("edit")
        }
        habitsService.addListener(habitsListener)
        val manager = LinearLayoutManager(this)

        recycler_view.adapter = habitAdapter
        recycler_view.layoutManager = manager
    }
}