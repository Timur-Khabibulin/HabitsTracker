package com.timurkhabibulin.myhabits.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.domain.Entities.PeriodType
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity
import kotlinx.android.synthetic.main.habit_item.view.*

class HabitsAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onDoneButtonClicked: (Long) -> Unit
) :
    ListAdapter<HabitPresentationEntity, HabitsViewHolder>(HabitDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return HabitsViewHolder(
            inflater.inflate(
                R.layout.habit_item,
                parent,
                false
            ),
            { itemPosition -> onItemClick(getItem(itemPosition).id) },
            { itemPosition -> onDoneButtonClicked(getItem(itemPosition).id) }
        )
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        val resources = holder.containerView.resources
        // val habitTypeToString = getBindedResources(holder.containerView.resources)
        val habit = getItem(position)

        with(holder.itemView) {
            val period =
                "${habit.executionNumberInPeriod} ${resources.getString(R.string.once_a)} ${
                    resources.getString(
                        periodTypeToResource(habit.periodType)
                    )
                }"
            if (habit.name.isNotEmpty()) habit_name_TV.text = habit.name
            if (habit.description.isNotEmpty()) habit_description_TV.text =
                habit.description
            habit_priority_TV.text = habit.priority.toString()
            habit_type_TV.text = resources.getString(habitTypeToResource(habit.type))
            habit_color_TV.setBackgroundColor(habit.color.toArgb())
            habit_period_TV.text = period
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