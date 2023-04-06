package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitService
import com.timurkhabibulin.myhabits.view.fragments.EditingFragmentMode

class HabitEditingViewModel(
    private val model: HabitService,
    private val mode: EditingFragmentMode,
    private val id: Int
) : ViewModel() {
    private val mutableHabit = MutableLiveData<Habit>()

    val habit = mutableHabit

    init {
        if (mode == EditingFragmentMode.EDIT) loadHabit()
    }

    private fun loadHabit() {
        mutableHabit.postValue(model.getHabit(id))
    }

    fun saveHabit(habit: Habit) {
        when (mode) {
            EditingFragmentMode.ADD -> HabitService.addHabit(habit)
            EditingFragmentMode.EDIT -> HabitService.changeItem(id, habit)
        }
    }
}