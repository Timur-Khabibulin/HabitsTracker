package com.timurkhabibulin.myhabits.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.activities.EditingActivityMode.*
import com.timurkhabibulin.myhabits.fragments.HabitEditingFragment


enum class EditingActivityMode {
    ADD, EDIT
}

const val ITEM_POSITION_PARAM = "itemPosition"

class HabitEditingActivity : AppCompatActivity() {
    private lateinit var activityMode: EditingActivityMode
    private var itemPosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_editing_activity)
        DynamicColors.applyToActivityIfAvailable(this)

        val inputData: Bundle? = intent.extras

        if (inputData != null) {
            activityMode = valueOf(
                inputData.getString(EditingActivityMode::class.java.toString()).toString()
            )
            itemPosition = inputData.getInt(ITEM_POSITION_PARAM) - 1
        }

        if (savedInstanceState == null) {
            val fragment = HabitEditingFragment.newInstance(activityMode.toString(), itemPosition)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.habit_editing_layout, fragment)
                .commit()
        }
    }
}