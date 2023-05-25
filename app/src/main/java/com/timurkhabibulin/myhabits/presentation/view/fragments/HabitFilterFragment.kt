package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.domain.Entities.HabitSortType
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitListViewModel
import com.timurkhabibulin.myhabits.presentation.viewmodel.SortDirection
import kotlinx.android.synthetic.main.fragment_habit_filter.*


class HabitFilterFragment : Fragment() {
    private lateinit var viewModel: HabitListViewModel
    private var sortDirection = SortDirection.ASCENDING
    private var sortType = HabitSortType.PRIORITY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]

        setUpSpinner()
        setUpSearchText()

        sort_ascend_IV.setOnClickListener {
            sortDirection = SortDirection.ASCENDING
            viewModel.sortHabits(sortType, sortDirection)
        }

        sort_desc_IV.setOnClickListener {
            sortDirection = SortDirection.DESCENDING
            viewModel.sortHabits(sortType, sortDirection)
        }
    }

    private fun setUpSpinner() {
        ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.sort_types)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            sort_type_spinner.adapter = arrayAdapter
        }
        sort_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sortType = HabitSortType.values()[position]
                viewModel.sortHabits(sortType, sortDirection)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setUpSearchText() {
        search_text.addTextChangedListener {
            viewModel.filterHabitsByName(it.toString())
        }
    }
}