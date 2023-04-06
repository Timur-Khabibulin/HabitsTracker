package com.timurkhabibulin.myhabits.viewmodel

import androidx.lifecycle.*
import com.timurkhabibulin.myhabits.model.Habit
import com.timurkhabibulin.myhabits.model.HabitService
import com.timurkhabibulin.myhabits.model.HabitSortType
import com.timurkhabibulin.myhabits.model.HabitType
import java.util.*

enum class SortDirection {
    ASCENDING, DESCENDING
}

class HabitListViewModel(
    private val model: HabitService
) : ViewModel() {
    private val mutableGoodHabits = MutableLiveData<MutableList<Habit>>()
    private val mutableBadHabits = MutableLiveData<MutableList<Habit>>()

    private var mutableFilteredGoodHabits = MutableLiveData<MutableList<Habit>>()
    private var mutableFilteredBadHabits = MutableLiveData<MutableList<Habit>>()

    val goodHabits = mutableGoodHabits
    val badHabits = mutableBadHabits


    fun loadHabits(type: HabitType) {
        model.loadHabits { habits ->
            when (type) {
                HabitType.GOOD -> mutableGoodHabits.postValue(habits
                    .filter { x -> x.type == type }
                    .toMutableList())

                HabitType.BAD -> mutableBadHabits.postValue(habits
                    .filter { x -> x.type == HabitType.BAD }
                    .toMutableList())
            }
        }
      //  filterHabitsByName("")
    }

    fun filterHabitsByName(name: String) {
       // mutableFilteredGoodHabits.filterAndPostHabits(mutableGoodHabits, name)
       // mutableFilteredBadHabits.filterAndPostHabits(mutableBadHabits, name)

        /*   val filteredGoodhabits = filt2(mutableGoodHabits, name)
           if (filteredGoodhabits.value != null)
               mutableFilteredGoodHabits.postValue(filteredGoodhabits.value)

           val filteredBadhabits = filt2(mutableBadHabits, name)
           if (filteredBadhabits.value != null)
               mutableFilteredBadHabits.postValue(filteredBadhabits.value)

         mutableFilteredGoodHabits = filt2(mutableGoodHabits, name).toMutableLiveData()
         mutableFilteredBadHabits = filt2(mutableBadHabits, name).toMutableLiveData()*/
    }

    fun sortHabits(type: HabitSortType, sortDirection: SortDirection) {
        mutableGoodHabits.sortAndPostValue(type, sortDirection)
        mutableBadHabits.sortAndPostValue(type, sortDirection)
    }

    private fun MutableLiveData<MutableList<Habit>>.sortAndPostValue(
        type: HabitSortType,
        sortDirection: SortDirection
    ) {
        if (this.value != null)
            this.postValue(
                getSortedHabits(
                    this.value as MutableList<Habit>,
                    type,
                    sortDirection
                )
            )
    }

    private fun getSortedHabits(
        list: MutableList<Habit>,
        type: HabitSortType,
        sortDirection: SortDirection
    ): MutableList<Habit> {
        return when (sortDirection) {
            SortDirection.ASCENDING -> list.sortedBy {
                when (type) {
                    HabitSortType.PRIORITY -> it.priority
                    HabitSortType.EXECUTION_NUMBER -> it.executionNumber
                    HabitSortType.PERIOD_NUMBER -> it.periodNumber
                }
            }.toMutableList()
            SortDirection.DESCENDING -> list.sortedByDescending {
                when (type) {
                    HabitSortType.PRIORITY -> it.priority
                    HabitSortType.EXECUTION_NUMBER -> it.executionNumber
                    HabitSortType.PERIOD_NUMBER -> it.periodNumber
                }
            }.toMutableList()
        }
    }

    private fun MutableLiveData<MutableList<Habit>>.filterAndPostHabits(
        dataToFilter: MutableLiveData<MutableList<Habit>>,
        name: String
    ) {
        var filteredList = mutableListOf<Habit>()
        if (dataToFilter.value != null) {
            if (name.isEmpty()) filteredList = dataToFilter.value!!
            else dataToFilter.value?.filter {
                it.name.lowercase(Locale.getDefault()).contains(name)
            }?.forEach { filteredList.add(it) }

            this.postValue(filteredList)
        }
    }

    /* fun filt2(
         dataToFilter: MutableLiveData<MutableList<Habit>>,
         name: String
     ): LiveData<MutableList<Habit>> {
         return Transformations.map(dataToFilter) {
             it?.filter {
                 name.lowercase(Locale.getDefault()).contains(name)
             }?.toMutableList()
         }
     }*/

    /*  private fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
          val mediatorLiveData = MediatorLiveData<T>()
          mediatorLiveData.addSource(this) {
              mediatorLiveData.value = it
          }
          return mediatorLiveData
      }*/

}