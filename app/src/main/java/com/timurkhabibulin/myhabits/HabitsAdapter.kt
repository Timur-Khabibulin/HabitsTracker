package com.timurkhabibulin.myhabits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class HabitsAdapter(private val onItemClick: () -> Unit) :
    RecyclerView.Adapter<HabitsViewHolder>() {

    var habits: List<Habit> = mutableListOf()
        set(newValue) {
            field = newValue
            //notifyDataSetChanged()
            notifyItemChanged(itemCount)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitsViewHolder(inflater.inflate(R.layout.habit_item, parent, false)){
            onItemClick()
        }
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habits[position])
    }

}

class HabitsViewHolder(
    override val containerView: View,
    onItemClicked: () -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        itemView.setOnClickListener {
            // this will be called only once.
            onItemClicked()
        }
    }

    fun bind(data: Habit) {
        val name = itemView.findViewById<TextView>(R.id.habit_name)
        name.text = data.name
    }

}