package com.timurkhabibulin.myhabits.view

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitType
import kotlinx.android.synthetic.main.habit_item.view.*

class HabitsAdapter(
    private val onItemClick: (Int) -> Unit,
) :
    ListAdapter<Habit, HabitsViewHolder>(HabitDiffCallBack()) {

    var habits: List<Habit> = mutableListOf()
    private lateinit var habitType: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return HabitsViewHolder(
            inflater.inflate(
                R.layout.habit_item,
                parent,
                false
            )
        ) { itemPosition -> onItemClick(habits[itemPosition].id) } //TODO: habit id or habit
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        val habitTypeToString = getBindedResources(holder.containerView.resources)
        val habit = habits[position]

        with(holder.itemView) {
            val period =
                "${habit.periodNumber} ${resources.getString(R.string.once_a)} ${habit.periodType}"
            habit_name.text =
                habit.name.ifEmpty { resources.getString(R.string.no_title) }
            textView2.text = habit.priority.toString()
            textView5.text = habitTypeToString[habit.type]
            habit_color.setBackgroundColor(habit.color.toArgb())
            textView9.text = period
            description_TV.text =
                habit.description.ifEmpty { resources.getString(R.string.no_description) }
        }
    }

    fun getBindedResources(resources: Resources) =
        mapOf(
            HabitType.GOOD to resources.getString(R.string.good_habit_type),
            HabitType.BAD to resources.getString(R.string.bad_habit_type)
        )

}