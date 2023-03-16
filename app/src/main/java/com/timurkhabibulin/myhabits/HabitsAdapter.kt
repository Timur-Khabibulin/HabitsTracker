package com.timurkhabibulin.myhabits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlinx.android.synthetic.main.habit_item.view.*

class HabitsAdapter(private val onItemClick: (Int) -> Unit) :
    ListAdapter<Habit, HabitsViewHolder>(HabitDiffCallBack()) {

    var habits: List<Habit> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitsViewHolder(
            inflater.inflate(
                R.layout.habit_item,
                parent,
                false
            )
        ) { itemPosition ->
            onItemClick(itemPosition)
        }
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        val habit = habits[position]

        with(holder.itemView) {
            val period =
                habit.periodNumber.toString() + " " + resources.getString(R.string.once_a) + " " + habit.periodType
            habit_name.text =
                habit.name.ifEmpty { resources.getString(R.string.no_title) }
            textView2.text = habit.priority.toString()
            textView5.text = habit.type
            habit_color.setBackgroundColor(habit.color.toArgb())
            textView9.text = period
            description_TV.text =
                habit.description.ifEmpty { resources.getString(R.string.no_description) }
        }
    }

}