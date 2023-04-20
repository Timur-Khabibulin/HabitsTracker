package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitSortType
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.model.db.HabitsRepository


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

    init {
        loadHabits()
    }

    fun sortHabits(sortType: HabitSortType, sortDirection: SortDirection) {
        mutableGoodHabits.sortAndPut(sortType, sortDirection, goodHabits)
        mutableBadHabits.sortAndPut(sortType, sortDirection, badHabits)
    }

    fun filterHabitsByName(filter: String) {
        mutableGoodHabits.filterAndPut(filter, goodHabits)
        mutableBadHabits.filterAndPut(filter, badHabits)
    }

    fun addObservers(
        habitType: HabitType,
        lifecycleOwner: LifecycleOwner,
        observer: Observer<List<Habit>?>
    ) {
        when (habitType) {
            HabitType.GOOD -> goodHabits.observe(lifecycleOwner, observer)
            HabitType.BAD -> badHabits.observe(lifecycleOwner, observer)
        }
    }

    fun removeObservers(
        habitType: HabitType,
        lifecycleOwner: LifecycleOwner,
    ) {
        when (habitType) {
            HabitType.GOOD -> goodHabits.removeObservers(lifecycleOwner)
            HabitType.BAD -> badHabits.removeObservers(lifecycleOwner)
        }
    }

    private fun loadHabits() {
        badHabits.addSource(mutableBadHabits) {
            badHabits.postValue(it)
        }
        goodHabits.addSource(mutableGoodHabits) {
            goodHabits.postValue(it)
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
