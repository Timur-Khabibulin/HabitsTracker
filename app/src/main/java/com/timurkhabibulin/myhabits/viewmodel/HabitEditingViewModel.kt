package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.db.HabitsRepository
import com.timurkhabibulin.myhabits.view.fragments.EditingFragmentMode

class HabitEditingViewModel(
    private val repository: HabitsRepository,
    private val mode: EditingFragmentMode,
    id: Int
) : ViewModel() {
    private val mutableHabit = MutableLiveData<Habit>()

    lateinit var habit: LiveData<Habit>

    init {
        if (mode == EditingFragmentMode.EDIT) {
            habit = repository.findById(id)
        }
    }

    fun saveState(habit: Habit) {
        mutableHabit.postValue(habit)
    }

    fun saveHabit(habit: Habit) {
        when (mode) {
            EditingFragmentMode.ADD -> repository.insert(habit)
            EditingFragmentMode.EDIT -> repository.update(habit)
        }
    }
}