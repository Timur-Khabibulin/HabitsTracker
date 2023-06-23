package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.timurkhabibulin.myhabits.databinding.FragmentAboutAppBinding

const val ABOUT_APP_FRAGMENT_NAME = "AboutAppFragment"

class AboutAppFragment : Fragment() {

    private var _binding: FragmentAboutAppBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = AboutAppFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}