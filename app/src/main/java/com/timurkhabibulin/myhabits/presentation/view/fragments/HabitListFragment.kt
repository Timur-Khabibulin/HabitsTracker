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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurkhabibulin.myhabits.databinding.FragmentHabitListBinding
import com.timurkhabibulin.myhabits.domain.Entities.HabitType
import com.timurkhabibulin.myhabits.presentation.entities.HabitPresentationEntity
import com.timurkhabibulin.myhabits.presentation.view.HabitsAdapter
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitListViewModel
import kotlinx.coroutines.launch


class HabitListFragment : Fragment() {

    private var fragmentMode = HabitType.GOOD
    private var _binding: FragmentHabitListBinding? = null
    private val binding
        get() = _binding!!

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpHabitList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.doneHabitMessage.collect {
                    Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.recyclerView.adapter = habitAdapter
        binding.recyclerView.layoutManager = manager
    }

    private fun onHabitsChanged(habits: List<HabitPresentationEntity>?) {
        habitAdapter.submitList(habits)
    }

    private fun openHabitEditingFragment(itemID: Long) {
        val action = MenuFragmentDirections
            .actionMenuFragment3ToHabitEditingFragment2(EditingFragmentMode.EDIT.toString(), itemID)
        findNavController().navigate(action)
    }
}