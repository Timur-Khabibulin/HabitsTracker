package com.timurkhabibulin.myhabits.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.timurkhabibulin.myhabits.databinding.HabitItemBinding
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity

class HabitsAdapter(
    private val onItemClick: (Long) -> Unit,
    private val onDoneButtonClicked: (Long) -> Unit
) :
    ListAdapter<HabitPresentationEntity, HabitsViewHolder>(HabitDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val itemBinding =
            HabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HabitsViewHolder(
            itemBinding,
            { itemPosition -> onItemClick(getItem(itemPosition).id) },
            { itemPosition -> onDoneButtonClicked(getItem(itemPosition).id) }
        )
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) =
        holder.bind(getItem(position))

}