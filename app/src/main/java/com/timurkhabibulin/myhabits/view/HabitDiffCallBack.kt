package com.timurkhabibulin.myhabits.view

import androidx.recyclerview.widget.DiffUtil
import com.timurkhabibulin.myhabits.model.Habit

class HabitDiffCallBack : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean =
        oldItem == newItem
}