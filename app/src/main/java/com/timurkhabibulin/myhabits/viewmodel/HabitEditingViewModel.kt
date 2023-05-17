package com.timurkhabibulin.myhabits.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.network.HabitService
import com.timurkhabibulin.myhabits.view.fragments.EditingFragmentMode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitEditingViewModel(
    private val habitService: HabitService,
    private val mode: EditingFragmentMode,
    private val id: Long
) : ViewModel() {
    lateinit var openedHabit: LiveData<Habit>

    private val mutableHabit = MutableLiveData<Habit>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e("viewModelScope", "$exception")
        }


    init {
        if (mode == EditingFragmentMode.EDIT) loadHabit()
    }

    fun saveHabit(habit: Habit) =
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                when (mode) {
                    EditingFragmentMode.ADD -> habitService.addHabit(habit)
                    EditingFragmentMode.EDIT -> habitService.update(habit.apply {
                        networkID = openedHabit.value!!.networkID
                        internalID = openedHabit.value!!.internalID
                    })
                }
            }
        }

    fun saveState(habit: Habit) =
        viewModelScope.launch(coroutineExceptionHandler) {
            mutableHabit.postValue(habit)
        }

    private fun loadHabit() =
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                openedHabit = habitService.findById(id)
            }
        }
}