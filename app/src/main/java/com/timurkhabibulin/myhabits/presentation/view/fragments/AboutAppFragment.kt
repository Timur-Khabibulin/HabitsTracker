package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timurkhabibulin.myhabits.R

const val ABOUT_APP_FRAGMENT_NAME="AboutAppFragment"
class AboutAppFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AboutAppFragment()
    }

}