package com.timurkhabibulin.myhabits.presentation.view

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import kotlinx.android.synthetic.main.habit_item.view.*

class HabitsAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onDoneButtonClicked: (Long) -> Unit
) :
    ListAdapter<Habit, HabitsViewHolder>(HabitDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return HabitsViewHolder(
            inflater.inflate(
                R.layout.habit_item,
                parent,
                false
            ),
            { itemPosition -> onItemClick(getItem(itemPosition).internalID) },
            { itemPosition -> onDoneButtonClicked(getItem(itemPosition).internalID) }
        )
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        val habitTypeToString = getBindedResources(holder.containerView.resources)
        val habit = getItem(position)

        with(holder.itemView) {
            val period =
                "${habit.executionNumberInPeriod} ${resources.getString(R.string.once_a)} ${habit.periodType}"
            if (habit.name.isNotEmpty()) habit_name_TV.text = habit.name
            if (habit.description.isNotEmpty()) habit_description_TV.text = habit.description
            habit_priority_TV.text = habit.priority.toString()
            habit_type_TV.text = habitTypeToString[habit.type]
            habit_color_TV.setBackgroundColor(habit.color.toArgb())
            habit_period_TV.text = period
        }
    }

    private fun getBindedResources(resources: Resources) =
        mapOf(
            HabitType.GOOD to resources.getString(R.string.good_habit_type),
            HabitType.BAD to resources.getString(R.string.bad_habit_type)
        )

}