package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.db.HabitsRepository
import com.timurkhabibulin.myhabits.view.fragments.EditingFragmentMode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitEditingViewModel(
    private val repository: HabitsRepository,
    private val mode: EditingFragmentMode,
    private val id: Int
) : ViewModel() {
    lateinit var habit: LiveData<Habit>

    private val mutableHabit = MutableLiveData<Habit>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception -> throw exception }


    init {
        loadHabit()
    }

    fun saveHabit(habit: Habit) =
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                when (mode) {
                    EditingFragmentMode.ADD -> repository.insert(habit)
                    EditingFragmentMode.EDIT -> repository.update(habit)
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
                if (mode == EditingFragmentMode.EDIT) {
                    habit = repository.findById(id)
                }
            }
        }
}