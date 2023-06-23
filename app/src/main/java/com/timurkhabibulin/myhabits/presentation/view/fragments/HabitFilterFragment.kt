package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.databinding.FragmentHabitFilterBinding
import com.timurkhabibulin.myhabits.domain.Entities.HabitSortType
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitListViewModel
import com.timurkhabibulin.myhabits.presentation.viewmodel.SortDirection


class HabitFilterFragment : Fragment() {
    private lateinit var viewModel: HabitListViewModel
    private var sortDirection = SortDirection.ASCENDING
    private var sortType = HabitSortType.PRIORITY

    private var _binding: FragmentHabitFilterBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHabitFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]

        setUpSpinner()
        setUpSearchText()

        binding.sortAscendIV.setOnClickListener {
            sortDirection = SortDirection.ASCENDING
            viewModel.sortHabits(sortType, sortDirection)
        }

        binding.sortDescIV.setOnClickListener {
            sortDirection = SortDirection.DESCENDING
            viewModel.sortHabits(sortType, sortDirection)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSpinner() {
        ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            resources.getStringArray(R.array.sort_types)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            binding.sortTypeSpinner.adapter = arrayAdapter
        }
        binding.sortTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
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
        binding.searchText.addTextChangedListener {
            viewModel.filterHabitsByName(it.toString())
        }
    }
}