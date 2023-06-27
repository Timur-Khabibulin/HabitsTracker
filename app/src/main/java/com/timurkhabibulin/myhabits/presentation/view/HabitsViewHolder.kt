package com.timurkhabibulin.myhabits.presentation.view

import androidx.recyclerview.widget.RecyclerView
import com.timurkhabibulin.domain.Entities.HabitType
import com.timurkhabibulin.domain.Entities.PeriodType
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.databinding.HabitItemBinding
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity

class HabitsViewHolder(
    private val itemBinding: HabitItemBinding,
    onItemClicked: (Int) -> Unit,
    onDoneButtonClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    init {
        itemBinding.root.setOnClickListener {
            onItemClicked(adapterPosition)
        }
        itemBinding.doneBtn.setOnClickListener {
            onDoneButtonClicked(adapterPosition)
        }
    }

    fun bind(habit: HabitPresentationEntity) {
        with(itemBinding) {
            val res = root.resources
            val period =
                "${habit.executionNumberInPeriod} ${res.getString(R.string.once_a)} ${
                    res.getString(
                        periodTypeToResource(habit.periodType)
                    )
                }"
            if (habit.name.isNotEmpty()) habitNameTV.text = habit.name
            if (habit.description.isNotEmpty()) habitDescriptionTV.text =
                habit.description
            habitPriorityTV.text = habit.priority.toString()
            habitTypeTV.text = res.getString(habitTypeToResource(habit.type))
            habitColorTV.setBackgroundColor(habit.color.toArgb())
            habitPeriodTV.text = period
        }
    }

    private fun habitTypeToResource(type: HabitType): Int =
        when (type) {
            HabitType.GOOD -> R.string.good_habit_type
            HabitType.BAD -> R.string.bad_habit_type
        }

    private fun periodTypeToResource(type: PeriodType): Int =
        when (type) {
            PeriodType.DAY -> R.string.day
            PeriodType.WEEK -> R.string.week
            PeriodType.MONTH -> R.string.month
            PeriodType.YEAR -> R.string.year
        }
}