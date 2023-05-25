package com.timurkhabibulin.myhabits.presentation.viewmodel

import androidx.lifecycle.*
import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.HabitsUseCase
import com.timurkhabibulin.myhabits.presentation.view.fragments.EditingFragmentMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitEditingViewModel(
    private val habitsUseCase: HabitsUseCase,
    private val mode: EditingFragmentMode,
    private val id: Long
) : ViewModel() {
    lateinit var openedHabit: LiveData<Habit>

    private val mutableHabit = MutableLiveData<Habit>()

    init {
        if (mode == EditingFragmentMode.EDIT) loadHabit()
    }

    /*    TODO: HTTP FAILED: java.io.IOException: Canceled.
           Ошибка при отправке запроса на сервер, тк viewmodelScope завршает работу
           (сохранение происходит перед выходом из фрагмента)*/
    fun saveHabit(habit: Habit) =
        viewModelScope.launch(Dispatchers.IO) {
            when (mode) {
                EditingFragmentMode.ADD -> habitsUseCase.addHabit(habit)
                EditingFragmentMode.EDIT -> habitsUseCase.update(habit.apply {
                    networkID = openedHabit.value!!.networkID
                    internalID = openedHabit.value!!.internalID
                })
            }
        }

    fun saveState(habit: Habit) =
        viewModelScope.launch(Dispatchers.IO) {
            mutableHabit.postValue(habit)
        }

    private fun loadHabit() =
        viewModelScope.launch(Dispatchers.IO) {
                openedHabit = habitsUseCase.findById(id).asLiveData()
        }
}