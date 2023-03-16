package com.timurkhabibulin.myhabits

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.EditingActivityMode.*
import kotlinx.android.synthetic.main.habit_editing.*
import kotlinx.android.synthetic.main.habit_editing.view.*
import kotlin.math.round


enum class EditingActivityMode {
    ADD, EDIT
}

class HabitEditingActivity : AppCompatActivity() {
    private lateinit var activityMode: EditingActivityMode
    private var itemPosition = 0
    private lateinit var habitTypeToRB: Map<String, RadioButton>
    private lateinit var habitPeriodTypeToNumber: Map<String, Int>
    private var choosedColor = Color.valueOf(Color.WHITE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_editing)
        DynamicColors.applyToActivityIfAvailable(this)
        bindResourcesToId()

        val inputData: Bundle? = intent.extras

        if (inputData != null) {
            activityMode = valueOf(
                inputData.getString(EditingActivityMode::class.java.toString()).toString()
            )
            itemPosition = inputData.getInt("itemPosition") - 1
        }

        save_btn.setOnClickListener {
            if (onSave()) finish()
        }

        ArrayAdapter(
            this,
            R.layout.spinner_item,
            resources.getStringArray(R.array.habit_priorities)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            priority_spinner.adapter = arrayAdapter
        }

        ArrayAdapter(
            this,
            R.layout.spinner_item,
            resources.getStringArray(R.array.period)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            spinner6.adapter = arrayAdapter
        }

        close_button.setOnClickListener {
            finish()
        }
        makeColorSquares()

    }

    override fun onStart() {
        super.onStart()
        printColor(choosedColor)
        if (activityMode == EDIT) fillInTheFields()
    }

    private fun bindResourcesToId() {
        habitTypeToRB = mapOf(
            resources.getString(R.string.habit_type_1) to habit_type1_RB,
            resources.getString(R.string.habit_type_2) to habit_type2_RB,
            resources.getString(R.string.habit_type_3) to habit_type3_RB
        )

        habitPeriodTypeToNumber = mapOf(
            resources.getString(R.string.day) to 0,
            resources.getString(R.string.week) to 1,
            resources.getString(R.string.month) to 2,
            resources.getString(R.string.year) to 3
        )
    }

    private fun fillInTheFields() {
        val habit = HabitService.getHabit(itemPosition)
        name_ET.setText(habit.name)
        description_ET.setText(habit.description)
        habit_type_radio_group.check(habitTypeToRB[habit.type]!!.id)
        priority_spinner.setSelection(habit.priority - 1)
        editTextNumberDecimal.setText(habit.executionNumber.toString())
        editTextNumber2.setText(habit.periodNumber.toString())
        spinner6.setSelection(habitPeriodTypeToNumber[habit.periodType]!!)
        current_color.setBackgroundColor(habit.color.toArgb())
        printColor(habit.color)
    }


    private fun makeColorSquares() {
        val hueColors = mutableListOf<Int>()

        for (i in 0..360) {
            val color = Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f))
            hueColors.add(color)
        }

        val hue = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, hueColors.toIntArray())
        colors_layout.background = hue

        val squareWidth = 200
        val squareMargin = round(0.25 * 250).toInt()
        val gradientWidth = (squareWidth + squareMargin) * 16
        val gradientHeight = squareWidth + 2 * squareMargin
        val bitmap = colors_layout.background.toBitmap(gradientWidth, gradientHeight)
        var squareCenterX = squareWidth / 2
        val squareCenterY = squareWidth / 2

        for (i in 0..15) {
            val square = View(this)
            val params = LinearLayout.LayoutParams(squareWidth, squareWidth)
            params.setMargins(0, squareMargin, squareMargin, squareMargin)
            square.layoutParams = params
            colors_scroll.colors_layout.addView(square)
            val color = bitmap.getColor(squareCenterX, squareCenterY)
            square.setBackgroundColor(color.toArgb())
            square.setOnClickListener {
                choosedColor = color
                current_color.setBackgroundColor(choosedColor.toArgb())
                printColor(color)
            }
            squareCenterX += squareMargin + squareWidth
        }
    }

    private fun onSave(): Boolean {
        val habit = getHabit()

        if (habit != null) {
            when (activityMode) {
                ADD -> HabitService.addHabit(habit)
                EDIT -> HabitService.changeItem(itemPosition, habit)
            }
            return true
        }
        return false
    }

    private fun getHabit(): Habit? {
        if (habit_type_radio_group.checkedRadioButtonId == -1) {
            Toast.makeText(this, R.string.type_not_selected, Toast.LENGTH_SHORT).show()
            return null
        }
        if (editTextNumberDecimal.text.isEmpty()) {
            Toast.makeText(this, R.string.repet_num_not_specified, Toast.LENGTH_SHORT).show()
            return null
        }
        if (editTextNumber2.text.isEmpty()) {
            Toast.makeText(this, R.string.period_not_selected, Toast.LENGTH_SHORT).show()
            return null
        }

        val radioButton = findViewById<RadioButton>(habit_type_radio_group.checkedRadioButtonId)
        return Habit(
            name_ET.text.toString(),
            description_ET.text.toString(),
            priority_spinner.selectedItem.toString().toInt(),
            radioButton.text.toString(),
            editTextNumberDecimal.text.toString().toInt(),
            editTextNumber2.text.toString().toInt(),
            spinner6.selectedItem.toString(),
            choosedColor
        )
    }

    @SuppressLint("SetTextI18n")
    private fun printColor(color: Color) {
        val r = String.format("%.1f", color.red())
        val g = String.format("%.1f", color.green())
        val b = String.format("%.1f", color.blue())

        val hsvValues = floatArrayOf(0f, 0f, 0f)
        Color.colorToHSV(color.toArgb(), hsvValues)

        val h = String.format("%.1f", hsvValues[0])
        val s = String.format("%.1f", hsvValues[1])
        val v = String.format("%.1f", hsvValues[2])

        rgb_color.text = "R: $r    G: $g    B: $b"
        hsv_color.text = "H: $h    S: $s      V: $v  "
    }
}