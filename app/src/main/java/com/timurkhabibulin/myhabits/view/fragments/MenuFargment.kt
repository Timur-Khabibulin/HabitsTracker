package com.timurkhabibulin.myhabits.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.model.db.AppDatabase
import com.timurkhabibulin.myhabits.model.db.HabitsRepository
import com.timurkhabibulin.myhabits.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.toolbar.*

const val MENU_FRAGMENT_NAME = "MenuFargment"

@Suppress("UNCHECKED_CAST")
class MenuFargment : Fragment() {
    private lateinit var drawerToggle: ActionBarDrawerToggle

    companion object {
        @JvmStatic
        fun newInstance() = MenuFargment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createListFragmentViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addHomeFragment(HomeFragment.newInstance())
        setUpDrawerToggle()
        setNavItemSelectedListener()
    }

    private fun createListFragmentViewModel() {
        val db = AppDatabase.getDatabase(requireContext().applicationContext)
        val repository = HabitsRepository(db.habitDao())

        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HabitListViewModel(repository) as T
            }
        })[HabitListViewModel::class.java]
    }

    private fun addHomeFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .add(R.id.menuContent, fragment, HOME_FRAGMENT_NAME)
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
                        HomeFragment.newInstance(),
                        HOME_FRAGMENT_NAME
                    )
                R.id.aboutAppFragment -> replaceChildFragment(
                    AboutAppFragment.newInstance(),
                    ABOUT_APP_FRAGMENT_NAME
                )
            }
            true
        }
    }

    private fun replaceChildFragment(fragment: Fragment, tag: String) {
        childFragmentManager.beginTransaction()
            .replace(R.id.menuContent, fragment, tag)
            .commit()
    }
}