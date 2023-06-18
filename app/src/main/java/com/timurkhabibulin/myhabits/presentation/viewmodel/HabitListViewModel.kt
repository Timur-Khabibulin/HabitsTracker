package com.timurkhabibulin.myhabits.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.timurkhabibulin.myhabits.domain.Entities.HabitSortType
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.domain.HabitsUseCase
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class HabitListViewModel(
    private val habitsUseCase: HabitsUseCase
) : ViewModel() {

    private var sourceGoodHabits = MediatorLiveData<List<HabitPresentationEntity>>()
    private var mutableBadHabits = MediatorLiveData<List<HabitPresentationEntity>>()

    val goodHabits = MediatorLiveData<List<HabitPresentationEntity>>()
    val badHabits = MediatorLiveData<List<HabitPresentationEntity>>()

    val doneHabitMessage = habitsUseCase.doneHabit.map {
        return@map if (it.leftToDo > 0) {
            when (it.type) {
                HabitType.GOOD -> "Стоит выполнить еще ${it.leftToDo} раз"
                HabitType.BAD -> "Можете выполнить еще ${it.leftToDo} раз"
            }
        } else {
            when (it.type) {
                HabitType.GOOD -> "You are breathtaking!"
                HabitType.BAD -> "Хватит это делать"
            }
        }
    }

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
        viewModelScope.launch {
            habitsUseCase.habitWasDone(id)
        }
    }

    private fun loadHabits() =
        viewModelScope.launch(Dispatchers.IO) {
            sourceGoodHabits.addSource(habitsUseCase.getAll().asLiveData()) { habits ->
                sourceGoodHabits.postValue(habits
                    .filter { x -> x.type == HabitType.GOOD }
                    .map { return@map HabitPresentationEntity.fromHabit(it) }
                )
            }
            mutableBadHabits.addSource(habitsUseCase.getAll().asLiveData()) { habits ->
                mutableBadHabits.postValue(habits
                    .filter { x -> x.type == HabitType.BAD }
                    .map { return@map HabitPresentationEntity.fromHabit(it) }
                )
            }

            goodHabits.addSource(sourceGoodHabits) {
                goodHabits.postValue(it)
            }
            badHabits.addSource(mutableBadHabits) {
                badHabits.postValue(it)
            }
            habitsUseCase.synchronizeWithNetwork()
        }

    private fun MediatorLiveData<List<HabitPresentationEntity>>.sort(
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

    private fun MediatorLiveData<List<HabitPresentationEntity>>.filterAndPut(
        filter: String,
        putInto: MediatorLiveData<List<HabitPresentationEntity>>
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
