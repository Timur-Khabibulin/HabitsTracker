package com.timurkhabibulin.myhabits.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.domain.HabitsUseCase
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity
import com.timurkhabibulin.myhabits.presentation.view.fragments.EditingFragmentMode
import com.timurkhabibulin.domain.Entities.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitEditingViewModel(
    private val habitsUseCase: HabitsUseCase,
    private val mode: EditingFragmentMode,
    private val id: Long
) : ViewModel() {

    private var mutableOpenedHabit: MutableLiveData<HabitPresentationEntity> =
        MutableLiveData<HabitPresentationEntity>()

    val openedHabit: LiveData<HabitPresentationEntity> = mutableOpenedHabit

    private val mutableHabit = MutableLiveData<Habit>()
    private var netId = ""


    init {
        if (mode == EditingFragmentMode.EDIT) loadHabit()
    }

    fun saveHabit(habit: HabitPresentationEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            when (mode) {
                EditingFragmentMode.ADD -> habitsUseCase.addHabit(habit.toHabit())
                EditingFragmentMode.EDIT -> habitsUseCase.update(habit.toHabit().apply {
                    networkID = netId
                    internalID = id
                })
            }
        }

    fun saveState(habit: HabitPresentationEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            mutableHabit.postValue(habit.toHabit())
        }

    private fun loadHabit() =
        viewModelScope.launch(Dispatchers.IO) {
            habitsUseCase.findById(id).collect {
                netId = it.networkID
                mutableOpenedHabit.postValue(HabitPresentationEntity.fromHabit(it))
            }
        }
}