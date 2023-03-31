package com.timurkhabibulin.myhabits

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

class HabitsViewHolder(
    override val containerView: View,
    onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    init {
        containerView.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }
}