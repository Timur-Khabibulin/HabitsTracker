package com.timurkhabibulin.myhabits.presentation.view.fragments

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
import androidx.navigation.fragment.findNavController
import com.timurkhabibulin.myhabits.*
import com.timurkhabibulin.myhabits.databinding.FragmentHabitEditingBinding
import com.timurkhabibulin.myhabits.presentation.HabitsApp
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitEditingViewModel
import javax.inject.Inject
import kotlin.math.round

enum class EditingFragmentMode {
    ADD, EDIT
}

class HabitEditingFragment : Fragment() {
    @Inject
    lateinit var habitsUseCase: com.timurkhabibulin.domain.HabitsUseCase

    private var _binding: FragmentHabitEditingBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var fragmentMode: EditingFragmentMode
    private var itemID: Long = 0
    private lateinit var habitTypeToRB: Map<com.timurkhabibulin.domain.Entities.HabitType, RadioButton>

    private var chosenColor = Color.valueOf(Color.WHITE)

    private lateinit var viewModel: HabitEditingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = HabitEditingFragmentArgs.fromBundle(it)
            fragmentMode = EditingFragmentMode.valueOf(args.fragmentMode)
            itemID = args.itemPosition
        }

        createViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitEditingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fragmentMode == EditingFragmentMode.EDIT)
            viewModel.openedHabit.observe(viewLifecycleOwner, ::fillInTheFields)

        bindResourcesToId()

        with(binding) {
            closeButton.setOnClickListener {
                findNavController().navigateUp()
            }
            saveBtn.setOnClickListener {
                getNewHabit()?.let { habit -> viewModel.saveHabit(habit) }
            }
        }

        setUpPrioritySpinner()
        setUpPeriodSpinner()

        makeColorSquares()
    }

    override fun onPause() {
        super.onPause()
        if (fragmentMode == EditingFragmentMode.EDIT)
            saveFieldsState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        (requireActivity().application as HabitsApp).appComponent.inject(this)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitEditingViewModel(habitsUseCase, fragmentMode, itemID) as T
            }
        })[HabitEditingViewModel::class.java]
    }

    private fun saveFieldsState() {
        val habit = viewModel.openedHabit.value!!

        with(binding) {
            val radioButton =
                view?.findViewById<RadioButton>(habitTypeRadioGroup.checkedRadioButtonId)

            habit.name = nameET.text.toString()
            habit.priority = prioritySpinner.selectedItem.toString().toInt()
            habit.description = descriptionET.text.toString()
            habit.type = habitTypeToRB.filterValues { it == radioButton }.keys.first()
            if (editTextNumberDecimal.text.isNotEmpty())
                habit.totalExecutionNumber = editTextNumberDecimal.text.toString().toInt()
            if (editTextNumber2.text.isNotEmpty())
                habit.executionNumberInPeriod = editTextNumber2.text.toString().toInt()
            habit.periodType = com.timurkhabibulin.domain.Entities.PeriodType.values()[spinner6.selectedItemPosition]
            habit.color = chosenColor
        }

        viewModel.saveState(habit)
    }

    private fun setUpPrioritySpinner() {
        ArrayAdapter(
            activity?.baseContext!!,
            R.layout.spinner_item,
            resources.getStringArray(R.array.habit_priorities)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            binding.prioritySpinner.adapter = arrayAdapter
        }
    }

    private fun setUpPeriodSpinner() {
        ArrayAdapter(
            activity?.baseContext!!,
            R.layout.spinner_item,
            resources.getStringArray(R.array.period)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            binding.spinner6.adapter = arrayAdapter
        }
    }

    private fun bindResourcesToId() {
        habitTypeToRB = mapOf(
            com.timurkhabibulin.domain.Entities.HabitType.GOOD to binding.habitType1RB,
            com.timurkhabibulin.domain.Entities.HabitType.BAD to binding.habitType2RB,
        )
    }

    private fun fillInTheFields(habit: HabitPresentationEntity) {
        with(binding) {
            nameET.setText(habit.name)
            descriptionET.setText(habit.description)
            habitTypeRadioGroup.check(habitTypeToRB[habit.type]!!.id)
            prioritySpinner.setSelection(habit.priority - 1)
            editTextNumberDecimal.setText(habit.totalExecutionNumber.toString())
            editTextNumber2.setText(habit.executionNumberInPeriod.toString())
            spinner6.setSelection(habit.periodType.ordinal)
            currentColor.setBackgroundColor(habit.color.toArgb())
            printColorValue(habit.color)
            chosenColor = habit.color
        }

    }

    private fun makeColorSquares() {
        val hueColors = mutableListOf<Int>()

        for (i in 0..360) {
            val color = Color.HSVToColor(floatArrayOf(i.toFloat(), 1f, 1f))
            hueColors.add(color)
        }

        val hue = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, hueColors.toIntArray())
        binding.colorsLayout.background = hue

        val squareWidth = 200
        val squareMargin = round(0.25 * 250).toInt()
        val gradientWidth = (squareWidth + squareMargin) * 16
        val gradientHeight = squareWidth + 2 * squareMargin
        val bitmap = binding.colorsLayout.background.toBitmap(gradientWidth, gradientHeight)
        var squareCenterX = squareWidth / 2
        val squareCenterY = squareWidth / 2

        for (i in 0..15) {
            val square = View(activity)
            val params = LinearLayout.LayoutParams(squareWidth, squareWidth)
            params.setMargins(0, squareMargin, squareMargin, squareMargin)
            square.layoutParams = params
            binding.colorsLayout.addView(square)
            val color = bitmap.getColor(squareCenterX, squareCenterY)
            square.setBackgroundColor(color.toArgb())
            square.setOnClickListener {
                chosenColor = color
                binding.currentColor.setBackgroundColor(chosenColor.toArgb())
                printColorValue(color)
            }
            squareCenterX += squareMargin + squareWidth
        }
    }

    private fun getNewHabit(): HabitPresentationEntity? {
        with(binding) {
            if (habitTypeRadioGroup.checkedRadioButtonId == -1) {
                Toast.makeText(activity, R.string.type_not_selected, Toast.LENGTH_SHORT).show()
                return null
            }
            if (editTextNumberDecimal.text.isEmpty()) {
                Toast.makeText(activity, R.string.repet_num_not_specified, Toast.LENGTH_SHORT)
                    .show()
                return null
            }
            if (editTextNumber2.text.isEmpty()) {
                Toast.makeText(activity, R.string.period_not_selected, Toast.LENGTH_SHORT).show()
                return null
            }
            val radioButton =
                habitTypeRadioGroup.findViewById<RadioButton>(habitTypeRadioGroup.checkedRadioButtonId)

            return HabitPresentationEntity(
                nameET.text.toString(),
                descriptionET.text.toString(),
                prioritySpinner.selectedItem.toString().toInt(),
                com.timurkhabibulin.domain.Entities.HabitType.values()[habitTypeRadioGroup.indexOfChild(radioButton) - 1],
                editTextNumberDecimal.text.toString().toInt(),
                editTextNumber2.text.toString().toInt(),
                com.timurkhabibulin.domain.Entities.PeriodType.values()[spinner6.selectedItemPosition],
                chosenColor,
            )
        }
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

        binding.rgbColor.text = "R: $r    G: $g    B: $b"
        binding.hsvColor.text = "H: $h    S: $s      V: $v  "
    }
}