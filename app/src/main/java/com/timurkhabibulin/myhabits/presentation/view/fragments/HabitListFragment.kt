package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity
import com.timurkhabibulin.myhabits.presentation.view.HabitsAdapter
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_habit_list.*
import kotlinx.coroutines.launch

//const val HABIT_LIST_FRAGMENT = "HabitListFragment"

class HabitListFragment : Fragment() {

    private var fragmentMode = HabitType.GOOD

    private lateinit var habitAdapter: HabitsAdapter

    private lateinit var viewModel: HabitListViewModel

    companion object {

        @JvmStatic
        fun newInstance(displayMode: HabitType) =
            HabitListFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        HabitType::class.java.toString(),
                        displayMode.toString()
                    )
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val actModeStr = it.getString(HabitType::class.java.toString()) ?: ""
            fragmentMode = HabitType.valueOf(actModeStr)
        }

        viewModel = ViewModelProvider(requireActivity())[HabitListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHabitList()
    }

    private fun setUpHabitList() {
        val manager = LinearLayoutManager(activity)

        habitAdapter = HabitsAdapter(
            { itemID -> openHabitEditingFragment(itemID) },
            { itemID -> viewModel.habitWasDone(itemID) }
        )

        when (fragmentMode) {
            HabitType.GOOD -> viewModel.goodHabits.observe(viewLifecycleOwner, ::onHabitsChanged)
            HabitType.BAD -> viewModel.badHabits.observe(viewLifecycleOwner, ::onHabitsChanged)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.doneHabitMessage.collect {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        recycler_view.adapter = habitAdapter
        recycler_view.layoutManager = manager
    }

    private fun onHabitsChanged(habits: List<HabitPresentationEntity>?) {
        habitAdapter.submitList(habits)
    }

    private fun openHabitEditingFragment(itemID: Long) {
        val fragment = HabitEditingFragment.newInstance(EditingFragmentMode.EDIT.toString(), itemID)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setReorderingAllowed(true)
            ?.replace(R.id.rootFragment, fragment, HABIT_EDITING_FRAGMENT_NAME)
            ?.commit()
    }
}