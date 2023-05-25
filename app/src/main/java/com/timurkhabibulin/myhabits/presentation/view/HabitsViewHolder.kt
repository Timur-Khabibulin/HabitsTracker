package com.timurkhabibulin.myhabits.presentation.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.habit_item.view.*

class HabitsViewHolder(
    override val containerView: View,
    onItemClicked: (Int) -> Unit,
    onDoneButtonClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        containerView.setOnClickListener {
            onItemClicked(adapterPosition)
        }
        containerView.done_btn.setOnClickListener {
            onDoneButtonClicked(adapterPosition)
        }
    }
}