package com.timurkhabibulin.myhabits

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.habit_editing.*
import kotlinx.android.synthetic.main.habit_editing.view.*
import kotlin.math.round

class HabitEditingActivity : AppCompatActivity() {
    lateinit var prioritySpinnerAdapter: ArrayAdapter<CharSequence>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_editing)

        val inputData: Bundle? = intent.extras

        if (inputData != null) {
            val type = inputData.getString("type").toString()
            //Toast.makeText(this, type, Toast.LENGTH_SHORT).show()
        }

        save_btn.setOnClickListener() { onSave() }

        clearTextWhenFirstChange(name_ET)
        clearTextWhenFirstChange(description_ET)
        clearTextWhenFirstChange(execution_number_ET)
        clearTextWhenFirstChange(period_ET)

        prioritySpinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.habit_priorities,
            R.layout.spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            priority_spinner.adapter = arrayAdapter
        }

        close_button.setOnClickListener() {
            openMainActivity()
        }

        makeColorSquares()

    }

    private fun makeColorSquares() {
        // var colors = mutableMapOf<View, Int>()
        val width = 200;
        val margin = round(0.25 * 250).toInt()
        for (i in 0..15) {
            val square = View(this)
            val params = LinearLayout.LayoutParams(width, width)
            params.setMargins(0, margin, margin, margin)
            square.setBackgroundColor(0xFF00FF0)
            square.layoutParams = params
            colors_scroll.colors_layout.addView(square)
            /* colors.put(square, 0xFF00FF0 + i * 2)
        colors.keys.last().setOnClickListener() {
            Toast.makeText(this, it.id.toString(), Toast.LENGTH_SHORT).show()
        }*/
        }
    }

    private fun clearTextWhenFirstChange(editText: EditText) {
        var flag = false

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (!flag) {
                flag = true
                editText.setText("")
            }
        }
    }

    private fun onSave() {
        val habit = getHabit()

        if (habit != null) {
            HabitService.addHabit(habit)
            openMainActivity()
        }
    }

    private fun openMainActivity() {
        val sendIntent = Intent(this, MainActivity::class.java)
        startActivity(sendIntent)
        finish()
    }

    private fun getHabit(): Habit? {
        if (habit_type_radio_group.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Тип не выбран", Toast.LENGTH_SHORT).show()
            return null
        }
        val name = name_ET.text.toString()
        val priority = priority_spinner.selectedItem.toString().toInt()
        val radioButton = findViewById<RadioButton>(habit_type_radio_group.checkedRadioButtonId)
        // Log.i("SavedHabit", name.toString() + priority.toString()+radioButton.text.toString())
        return Habit(name, priority, radioButton.text.toString())
    }
}