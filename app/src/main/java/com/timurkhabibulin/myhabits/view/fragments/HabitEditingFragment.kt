package com.timurkhabibulin.myhabits.view.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.GsonBuilder
import com.timurkhabibulin.myhabits.*
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.db.AppDatabase
import com.timurkhabibulin.myhabits.db.HabitsRepository
import com.timurkhabibulin.myhabits.network.*
import com.timurkhabibulin.myhabits.viewmodel.HabitEditingViewModel
import kotlinx.android.synthetic.main.fragment_habit_editing.*
import kotlinx.android.synthetic.main.fragment_habit_editing.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round

enum class EditingFragmentMode {
    ADD, EDIT
}

const val ITEM_ID_PARAM = "itemID"
const val EDITING_FRAGMENT_MODE_PARAM = "EditingFragmentMode"
const val HABIT_EDITING_FRAGMENT_NAME = "HabitEditingFragment"

class HabitEditingFragment : Fragment() {

    private lateinit var fragmentMode: EditingFragmentMode
    private var itemID: Long = 0
    private lateinit var habitTypeToRB: Map<HabitType, RadioButton>
    private lateinit var habitPeriodTypeToNumber: Map<String, Int>
    private var chosenColor = Color.valueOf(Color.WHITE)

    private lateinit var viewModel: HabitEditingViewModel

    companion object {
        @JvmStatic
        fun newInstance(activityMode: String, itemPosition: Long) =
            HabitEditingFragment().apply {
                arguments = Bundle().apply {
                    putString(EDITING_FRAGMENT_MODE_PARAM, activityMode)
                    putLong(ITEM_ID_PARAM, itemPosition)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(EDITING_FRAGMENT_MODE_PARAM) ?: ""
            fragmentMode = EditingFragmentMode.valueOf(actModeStr)
            itemID = it.getLong(ITEM_ID_PARAM)
        }

        createViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_editing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fragmentMode == EditingFragmentMode.EDIT)
            viewModel.openedHabit.observe(viewLifecycleOwner, ::fillInTheFields)

        bindResourcesToId()

        onCloseFragment()

        setUpPrioritySpinner()
        setUpPeriodSpinner()

        makeColorSquares()
    }

    override fun onPause() {
        super.onPause()
        if (fragmentMode == EditingFragmentMode.EDIT)
            saveFieldsState()
    }

    private fun createViewModel() {
        val db = AppDatabase.getDatabase(requireContext().applicationContext)
        val networkApi = createNetworkApi()

        val repository = HabitsRepository(db.habitDao())
        val habitService = HabitService(repository, networkApi)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitEditingViewModel(habitService, fragmentMode, itemID) as T
            }
        })[HabitEditingViewModel::class.java]
    }

    private fun createNetworkApi(): NetworkApi {
        val gson = GsonBuilder()
            .registerTypeAdapter(HabitNetworkEntity::class.java, HabitJsonDeserializer())
            .registerTypeAdapter(String::class.java, HabitUIDJsonDeserializer())
            .registerTypeAdapter(HabitNetworkEntity::class.java, HabitJsonSerializer())
            .create()

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://droid-test-server.doubletapp.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(NetworkApi::class.java)
    }

    private fun saveFieldsState() {
        val habit = viewModel.openedHabit.value!!
        val radioButton =
            view?.findViewById<RadioButton>(habit_type_radio_group.checkedRadioButtonId)
        habit.name = name_ET.text.toString()
        habit.description = description_ET.text.toString()
        habit.priority = priority_spinner.selectedItem.toString().toInt()
        habit.type = habitTypeToRB.filterValues { it == radioButton }.keys.first()
        if (editTextNumberDecimal.text.isNotEmpty())
            habit.executionNumber = editTextNumberDecimal.text.toString().toInt()
        if (editTextNumber2.text.isNotEmpty())
            habit.periodNumber = editTextNumber2.text.toString().toInt()
        habit.periodType = spinner6.selectedItem.toString()
        habit.color = chosenColor

        viewModel.saveState(habit)
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
            val habit = getNewHabit()
            if (habit != null) {
                viewModel.saveHabit(habit)
                // openMenuFragment()
            }
        }
        close_button.setOnClickListener { openMenuFragment() }
    }

    private fun openMenuFragment() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.rootFragment, MenuFargment.newInstance())
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