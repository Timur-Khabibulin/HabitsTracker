package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.HabitType
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.toolbar.*

const val MENU_FRAGMENT_NAME = "MenuFargment"

class MenuFargment : Fragment() {
    private lateinit var drawerToggle: ActionBarDrawerToggle

    companion object {

        @JvmStatic
        fun newInstance() = MenuFargment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addChildFragment(HomeFragment.newInstance(HabitType.GOOD), HOME_FRAGMENT_NAME)
        setUpDrawerToggle()
        setNavItemSelectedListener()
    }

    private fun addChildFragment(fragment: Fragment, tag: String) {
        childFragmentManager
            .beginTransaction()
            .add(R.id.contentFrame, fragment, tag)
            .commit()
    }

    private fun setUpDrawerToggle() {
        drawerToggle = ActionBarDrawerToggle(
            activity,
            menuLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        menuLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


    private fun setNavItemSelectedListener() {
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment ->
                    replaceChildFragment(
                        HomeFragment.newInstance(HabitType.GOOD),
                        HOME_FRAGMENT_NAME
                    )
                R.id.aboutAppFragment -> replaceChildFragment(
                    AboutAppFragment.newInstance(),
                    ABOUT_APP_FRAGMENT_NAME
                )
                else -> false
            }
            true
        }
    }

    private fun replaceChildFragment(fragment: Fragment, tag: String) {
        childFragmentManager.beginTransaction()
            .replace(R.id.contentFrame, fragment, tag)
            .commit()
    }
}