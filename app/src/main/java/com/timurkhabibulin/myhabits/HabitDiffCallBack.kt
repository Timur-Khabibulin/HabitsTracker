package com.timurkhabibulin.myhabits

import androidx.recyclerview.widget.DiffUtil

class HabitDiffCallBack : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean =
        oldItem == newItem
}