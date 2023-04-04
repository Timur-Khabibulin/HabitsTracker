package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitService
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.model.HabitsListener

class HabitListViewModel(
    private val model: HabitService,
    private val habitsType: HabitType
) : ViewModel() {

    private val mutableHabits = MutableLiveData<MutableList<Habit>>()

    val habits = mutableHabits

    init {
        load()
    }

    private fun load() {
        model.loadHabits { habits ->
            mutableHabits.postValue(habits
                .filter { x -> x.type == habitsType }
                .toMutableList())
        }
    }
}