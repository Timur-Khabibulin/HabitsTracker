package com.timurkhabibulin.myhabits.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitSortType
import com.timurkhabibulin.myhabits.model.HabitType
import com.timurkhabibulin.myhabits.network.HabitService
import kotlinx.coroutines.*


class HabitListViewModel(
    private val habitService: HabitService
) : ViewModel() {

    private var sourceGoodHabits = MediatorLiveData<List<Habit>>()
    private var mutableBadHabits = MediatorLiveData<List<Habit>>()

    private val goodHabits = MediatorLiveData<List<Habit>>()
    private val badHabits = MediatorLiveData<List<Habit>>()

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, exception ->
            Log.e("viewModelScope", "$exception")
        }

    init {
        loadHabits()
    }

    fun sortHabits(sortType: HabitSortType, sortDirection: SortDirection) =
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.Default) {
                goodHabits.sort(sortType, sortDirection)
                badHabits.sort(sortType, sortDirection)
            }
        }

    fun filterHabitsByName(filter: String) =
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.Default) {
                sourceGoodHabits.filterAndPut(filter, goodHabits)
                mutableBadHabits.filterAndPut(filter, badHabits)
            }
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

                sourceGoodHabits.addSource(habitService.getAll().asLiveData()) {
                    sourceGoodHabits.postValue(it.filter { x -> x.type == HabitType.GOOD })
                }
                mutableBadHabits.addSource(habitService.getAll().asLiveData()) {
                    mutableBadHabits.postValue(it.filter { x -> x.type == HabitType.BAD })
                }

                goodHabits.addSource(sourceGoodHabits) {
                    goodHabits.postValue(it)
                }
                badHabits.addSource(mutableBadHabits) {
                    badHabits.postValue(it)
                }
                habitService.synchronizeWithNetwork()
            }
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
                        HabitSortType.EXECUTION_NUMBER -> habit.executionNumber
                        HabitSortType.PERIOD_NUMBER -> habit.periodNumber
                    }
                }
                SortDirection.DESCENDING -> it.sortedByDescending { habit ->
                    when (sortType) {
                        HabitSortType.PRIORITY -> habit.priority
                        HabitSortType.EXECUTION_NUMBER -> habit.executionNumber
                        HabitSortType.PERIOD_NUMBER -> habit.periodNumber
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
