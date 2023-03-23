package com.timurkhabibulin.myhabits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.timurkhabibulin.myhabits.fragments.HabitListDisplayMode
import com.timurkhabibulin.myhabits.habitModel.Habit
import kotlinx.android.synthetic.main.habit_item.view.*

class HabitsAdapter(
    private val displayMode: HabitListDisplayMode,
    private val onItemClick: (Int) -> Unit,
) :
    ListAdapter<Habit, HabitsViewHolder>(HabitDiffCallBack()) {

    var habits: List<Habit> = mutableListOf()
    private lateinit var habitType: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        habitType = when (displayMode) {
            HabitListDisplayMode.GOOD_HABITS -> parent.context.getString(R.string.good_habit_type)
            HabitListDisplayMode.BAD_HABITS -> parent.context.getString(R.string.bad_habit_type)
        }

        return HabitsViewHolder(
            inflater.inflate(
                R.layout.habit_item,
                parent,
                false
            )
        ) { itemPosition -> onItemClick(itemPosition) }
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        val habit = habits[position]

        with(holder.itemView) {
            val period =
                "${habit.periodNumber} ${resources.getString(R.string.once_a)} ${habit.periodType}"
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