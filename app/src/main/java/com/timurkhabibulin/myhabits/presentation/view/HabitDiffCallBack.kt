package com.timurkhabibulin.myhabits.presentation.view

import androidx.recyclerview.widget.DiffUtil
import com.timurkhabibulin.myhabits.domain.Entities.Habit

class HabitDiffCallBack : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean =
        oldItem.internalID == newItem.internalID

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean =
        oldItem == newItem
}