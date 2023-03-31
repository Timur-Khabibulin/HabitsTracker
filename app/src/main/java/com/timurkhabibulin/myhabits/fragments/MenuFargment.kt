package com.timurkhabibulin.myhabits.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.habitModel.HabitType
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import java.security.AccessController

const val MENU_FRAGMENT_NAME = "MenuFargment"

class MenuFargment : Fragment() {
    private lateinit var drawerToggle: ActionBarDrawerToggle
   // private lateinit var navController: NavController

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

//        navView.setupWithNavController(findNavController())
        //NavigationUI.setupActionBarWithNavController(requireActivity(), navView.findNavController(), drawerToggle)
        // navView.setupWithNavController(navController = )

        // val navHost = activity?.supportFragmentManager?.findFragmentById(R.id.root_layout) as NavHostFragment
        // navController = navHost.navController
        // NavigationUI.setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)

        /*      navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph, menuLayout)
        navView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(
            requireActivity() as AppCompatActivity,
            navController,
            appBarConfiguration
        )*/
    }

    private fun addChildFragment(fragment: Fragment, tag: String) {
        childFragmentManager
            .beginTransaction()
            .add(R.id.contentFrame, fragment, tag)
            .addToBackStack(tag)
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
                /*   R.id.homeFragment -> findNavController().navigate(
                       R.id.homeFragment,
                       Bundle().apply {
                           putString(HABIT_TYPE_PARAM, HabitType.GOOD.toString())
                       }
                   )*/
                R.id.homeFragment -> replaceChildFragment(
                    HomeFragment.newInstance(HabitType.GOOD),
                    HOME_FRAGMENT_NAME
                )
                /*   R.id.aboutAppFragment -> findNavController().navigate(
                       R.id.aboutAppFragment
                   )*/
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


    /* override fun onOptionsItemSelected(item: MenuItem): Boolean {
         return NavigationUI.onNavDestinationSelected(
             item!!,
             requireView().findNavController()
         ) || super.onOptionsItemSelected(item)
     }*/


}