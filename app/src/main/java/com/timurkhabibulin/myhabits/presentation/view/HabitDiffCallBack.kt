package com.timurkhabibulin.myhabits.presentation.view

import androidx.recyclerview.widget.DiffUtil
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity

class HabitDiffCallBack : DiffUtil.ItemCallback<HabitPresentationEntity>() {
    override fun areItemsTheSame(
        oldItem: HabitPresentationEntity,
        newItem: HabitPresentationEntity
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: HabitPresentationEntity,
        newItem: HabitPresentationEntity
    ): Boolean =
        oldItem == newItem
}