package com.timurkhabibulin.myhabits.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timurkhabibulin.myhabits.*
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitService
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.viewmodel.HabitEditingViewModel
import com.timurkhabibulin.myhabits.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_habit_editing.*
import kotlinx.android.synthetic.main.fragment_habit_editing.view.*
import kotlin.math.round

enum class EditingFragmentMode {
    ADD, EDIT
}

const val ITEM_ID_PARAM = "itemID"
const val EDITING_FRAGMENT_MODE_PARAM = "EditingFragmentMode"
const val HABIT_EDITING_FRAGMENT_NAME = "HabitEditingFragment"

class HabitEditingFragment : Fragment() {

    private lateinit var activityMode: EditingFragmentMode
    private var itemID = 0
    private lateinit var habitTypeToRB: Map<HabitType, RadioButton>
    private lateinit var habitPeriodTypeToNumber: Map<String, Int>
    private var chosenColor = Color.valueOf(Color.WHITE)

    private lateinit var viewModel: HabitEditingViewModel

    companion object {
        @JvmStatic
        fun newInstance(activityMode: String, itemPosition: Int) =
            HabitEditingFragment().apply {
                arguments = Bundle().apply {
                    putString(EDITING_FRAGMENT_MODE_PARAM, activityMode)
                    putInt(ITEM_ID_PARAM, itemPosition)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(EDITING_FRAGMENT_MODE_PARAM) ?: ""
            activityMode = EditingFragmentMode.valueOf(actModeStr)
            itemID = it.getInt(ITEM_ID_PARAM)
        }

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitEditingViewModel(HabitService, activityMode, itemID) as T
            }
        })[HabitEditingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_editing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.habit.observe(viewLifecycleOwner, ::fillInTheFields)

        bindResourcesToId()

        onCloseFragment()

        setUpPrioritySpinner()
        setUpPeriodSpinner()

        makeColorSquares()

    }

    private fun setUpPrioritySpinner() {
        ArrayAdapter(
            activity?.baseContext!!,
            R.layout.spinner_item,
            resources.getStringArray(R.array.habit_priorities)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            priority_spinner.adapter = arrayAdapter
        }
    }

    private fun setUpPeriodSpinner() {
        ArrayAdapter(
            activity?.baseContext!!,
            R.layout.spinner_item,
            resources.getStringArray(R.array.period)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            spinner6.adapter = arrayAdapter
        }
    }

    private fun bindResourcesToId() {
        habitTypeToRB = mapOf(
            HabitType.GOOD to habit_type1_RB,
            HabitType.BAD to habit_type2_RB,
        )

        habitPeriodTypeToNumber = mapOf(
            resources.getString(R.string.day) to 0,
            resources.getString(R.string.week) to 1,
            resources.getString(R.string.month) to 2,
            resources.getString(R.string.year) to 3
        )
    }

    private fun fillInTheFields(habit: Habit) {
        name_ET.setText(habit.name)
        description_ET.setText(habit.description)
        habit_type_radio_group.check(habitTypeToRB[habit.type]!!.id)
        priority_spinner.setSelection(habit.priority - 1)
        editTextNumberDecimal.setText(habit.executionNumber.toString())
        editTextNumber2.setText(habit.periodNumber.toString())
        spinner6.setSelection(habitPeriodTypeToNumber[habit.periodType]!!)
        current_color.setBackgroundColor(habit.color.toArgb())
        printColorValue(habit.color)
        chosenColor = habit.color
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
            val square = View(activity)
            val params = LinearLayout.LayoutParams(squareWidth, squareWidth)
            params.setMargins(0, squareMargin, squareMargin, squareMargin)
            square.layoutParams = params
            colors_scroll.colors_layout.addView(square)
            val color = bitmap.getColor(squareCenterX, squareCenterY)
            square.setBackgroundColor(color.toArgb())
            square.setOnClickListener {
                chosenColor = color
                current_color.setBackgroundColor(chosenColor.toArgb())
                printColorValue(color)
            }
            squareCenterX += squareMargin + squareWidth
        }
    }

    private fun onCloseFragment() {
        save_btn.setOnClickListener {
            viewModel.saveHabit(getNewHabit())
            openMenuFragment()
        }
        close_button.setOnClickListener { openMenuFragment() }
    }

    private fun openMenuFragment() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.contentFrame, MenuFargment.newInstance())
            ?.commit()
    }

    private fun getNewHabit(): Habit? {
        if (habit_type_radio_group.checkedRadioButtonId == -1) {
            Toast.makeText(activity, R.string.type_not_selected, Toast.LENGTH_SHORT).show()
            return null
        }
        if (editTextNumberDecimal.text.isEmpty()) {
            Toast.makeText(activity, R.string.repet_num_not_specified, Toast.LENGTH_SHORT).show()
            return null
        }
        if (editTextNumber2.text.isEmpty()) {
            Toast.makeText(activity, R.string.period_not_selected, Toast.LENGTH_SHORT).show()
            return null
        }

        val radioButton =
            view?.findViewById<RadioButton>(habit_type_radio_group.checkedRadioButtonId)
        return Habit(
            HabitService.getNextId(),
            name_ET.text.toString(),
            description_ET.text.toString(),
            priority_spinner.selectedItem.toString().toInt(),
            habitTypeToRB.filterValues { it == radioButton }.keys.first(),
            editTextNumberDecimal.text.toString().toInt(),
            editTextNumber2.text.toString().toInt(),
            spinner6.selectedItem.toString(),
            chosenColor
        )
    }

    @SuppressLint("SetTextI18n")
    private fun printColorValue(color: Color) {
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