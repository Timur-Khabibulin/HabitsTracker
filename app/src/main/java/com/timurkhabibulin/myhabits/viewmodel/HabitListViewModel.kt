package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.*
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitSortType
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.model.db.HabitsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HabitListViewModel(
    repository: HabitsRepository
) : ViewModel() {

    private val mutableGoodHabits = Transformations.map(repository.getAll()) {
        it.filter { x -> x.type == HabitType.GOOD }
    }

    private val mutableBadHabits = Transformations.map(repository.getAll()) {
        it.filter { x -> x.type == HabitType.BAD }
    }

    private val goodHabits = MediatorLiveData<List<Habit>>()
    private val badHabits = MediatorLiveData<List<Habit>>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception -> throw exception }

    init {
        loadHabits()
    }

    fun sortHabits(sortType: HabitSortType, sortDirection: SortDirection) =
        viewModelScope.launch(coroutineExceptionHandler) {
            mutableGoodHabits.sortAndPut(sortType, sortDirection, goodHabits)
            mutableBadHabits.sortAndPut(sortType, sortDirection, badHabits)
        }

    fun filterHabitsByName(filter: String) =
        viewModelScope.launch(coroutineExceptionHandler) {
            mutableGoodHabits.filterAndPut(filter, goodHabits)
            mutableBadHabits.filterAndPut(filter, badHabits)
        }

    fun addObservers(
        habitType: HabitType,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<List<Habit>?>
    ) = viewModelScope.launch(coroutineExceptionHandler) {
        when (habitType) {
            HabitType.GOOD -> goodHabits.observe(lifecycleOwner, observer)
            HabitType.BAD -> badHabits.observe(lifecycleOwner, observer)
        }
    }

    private fun loadHabits() =
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                badHabits.addSource(mutableBadHabits) {
                    badHabits.postValue(it)
                }
                goodHabits.addSource(mutableGoodHabits) {
                    goodHabits.postValue(it)
                }
            }
        }

    private fun LiveData<List<Habit>>.sortAndPut(
        sortType: HabitSortType,
        sortDirection: SortDirection,
        putInto: MediatorLiveData<List<Habit>>
    ) {
        this.value.let {
            putInto.postValue(when (sortDirection) {
                SortDirection.ASCENDING -> it?.sortedBy { habit ->
                    when (sortType) {
                        HabitSortType.PRIORITY -> habit.priority
                        HabitSortType.EXECUTION_NUMBER -> habit.executionNumber
                        HabitSortType.PERIOD_NUMBER -> habit.periodNumber
                    }
                }
                SortDirection.DESCENDING -> it?.sortedByDescending { habit ->
                    when (sortType) {
                        HabitSortType.PRIORITY -> habit.priority
                        HabitSortType.EXECUTION_NUMBER -> habit.executionNumber
                        HabitSortType.PERIOD_NUMBER -> habit.periodNumber
                    }
                }
            })
        }
    }

    private fun LiveData<List<Habit>>.filterAndPut(
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
