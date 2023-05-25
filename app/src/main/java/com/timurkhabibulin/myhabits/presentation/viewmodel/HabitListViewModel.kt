package com.timurkhabibulin.myhabits.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.myhabits.domain.Entities.Habit
import com.timurkhabibulin.myhabits.domain.Entities.HabitSortType
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.domain.HabitsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HabitListViewModel(
    private val habitsUseCase: HabitsUseCase
) : ViewModel() {

    private var sourceGoodHabits = MediatorLiveData<List<Habit>>()
    private var mutableBadHabits = MediatorLiveData<List<Habit>>()

    val goodHabits = MediatorLiveData<List<Habit>>()
    val badHabits = MediatorLiveData<List<Habit>>()

    val doneHabitMessage = habitsUseCase.doneHabitMessage

    init {
        loadHabits()
    }

    fun sortHabits(sortType: HabitSortType, sortDirection: SortDirection) =
        viewModelScope.launch(Dispatchers.Default) {
            goodHabits.sort(sortType, sortDirection)
            badHabits.sort(sortType, sortDirection)
        }

    fun filterHabitsByName(filter: String) =
        viewModelScope.launch(Dispatchers.Default) {
            sourceGoodHabits.filterAndPut(filter, goodHabits)
            mutableBadHabits.filterAndPut(filter, badHabits)
        }

    fun habitWasDone(id: Long) {
        viewModelScope.launch() {
            habitsUseCase.habitWasDone(id)
        }
    }

    private fun loadHabits() =
        viewModelScope.launch(Dispatchers.IO) {
            sourceGoodHabits.addSource(habitsUseCase.getAll().asLiveData()) {
                sourceGoodHabits.postValue(it.filter { x -> x.type == HabitType.GOOD })
            }
            mutableBadHabits.addSource(habitsUseCase.getAll().asLiveData()) {
                mutableBadHabits.postValue(it.filter { x -> x.type == HabitType.BAD })
            }

            goodHabits.addSource(sourceGoodHabits) {
                goodHabits.postValue(it)
            }
            badHabits.addSource(mutableBadHabits) {
                badHabits.postValue(it)
            }
            habitsUseCase.synchronizeWithNetwork()
        }

    private fun MediatorLiveData<List<Habit>>.sort(
        sortType: HabitSortType,
        sortDirection: SortDirection
    ) {
        this.value?.let {
            this.postValue(when (sortDirection) {
                SortDirection.ASCENDING -> it.sortedBy { habit ->
                    when (sortType) {
                        HabitSortType.PRIORITY -> habit.priority
                        HabitSortType.EXECUTION_NUMBER -> habit.totalExecutionNumber
                        HabitSortType.PERIOD_NUMBER -> habit.executionNumberInPeriod
                    }
                }
                SortDirection.DESCENDING -> it.sortedByDescending { habit ->
                    when (sortType) {
                        HabitSortType.PRIORITY -> habit.priority
                        HabitSortType.EXECUTION_NUMBER -> habit.totalExecutionNumber
                        HabitSortType.PERIOD_NUMBER -> habit.executionNumberInPeriod
                    }
                }
            })
        }
    }

    private fun MediatorLiveData<List<Habit>>.filterAndPut(
        filter: String,
        putInto: MediatorLiveData<List<Habit>>
    ) {
        this.value.let {
            putInto.postValue(
                if (filter.isNotEmpty())
                    it?.filter { habit -> habit.name.lowercase().contains(filter) }
                else it
            )
        }
    }
}
