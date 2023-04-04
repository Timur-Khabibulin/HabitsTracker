package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.timurkhabibulin.myhabits.R
import kotlinx.android.synthetic.main.fragment_habit_filter.*


class HabitFilterFragment : Fragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    companion object {

        @JvmStatic
        fun newInstance() = HabitFilterFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(habitFilter)
        setUpSortTypeSpinner()
    }

    private fun setUpSortTypeSpinner(){
        ArrayAdapter(
            activity?.baseContext!!,
            R.layout.spinner_item,
            resources.getStringArray(R.array.sort_types)
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
            sort_type_spinner.adapter = arrayAdapter
        }
    }
}