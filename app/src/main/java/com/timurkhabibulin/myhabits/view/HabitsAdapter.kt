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
    // var habitsFiltered: List<Habit> = mutableListOf()

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
            if (habit.name.isNotEmpty()) habit_name_TV.text = habit.name
            if (habit.description.isNotEmpty()) habit_description_TV.text = habit.description
            habit_priority_TV.text = habit.priority.toString()
            habit_type_TV.text = habitTypeToString[habit.type]
            habit_color_TV.setBackgroundColor(habit.color.toArgb())
            habit_period_TV.text = period
        }
    }

    fun getBindedResources(resources: Resources) =
        mapOf(
            HabitType.GOOD to resources.getString(R.string.good_habit_type),
            HabitType.BAD to resources.getString(R.string.bad_habit_type)
        )

    /* override fun getFilter(): Filter {
         return object : Filter() {
             override fun performFiltering(nameConstraint: CharSequence?): FilterResults {
                 val constraint = nameConstraint?.toString() ?: ""

                 if (constraint.isEmpty()) habitsFiltered = habits
                 else {
                     val filteredList = mutableListOf<Habit>()

                     habits.filter {
                         it.name.lowercase(Locale.getDefault()).contains(constraint)
                     }.forEach {
                         filteredList.add(it)
                     }

                     habitsFiltered = filteredList
                 }

                 return FilterResults().apply { values = habitsFiltered }
             }

             override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                 habitsFiltered = if (results?.values == null) mutableListOf()
                 else results.values as MutableList<Habit>

                 submitList(habitsFiltered)
             }

         }
     }*/

}