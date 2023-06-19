package com.timurkhabibulin.myhabits.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.presentation.HabitsApp
import com.timurkhabibulin.myhabits.data.network.*
import com.timurkhabibulin.myhabits.domain.HabitsUseCase
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitListViewModel
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MenuFragment : Fragment() {
    @Inject
    lateinit var habitsUseCase: HabitsUseCase

    private lateinit var drawerToggle: ActionBarDrawerToggle

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
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

        setUpDrawerToggle()
        setNavItemSelectedListener()
    }

    private fun createListFragmentViewModel() {
        (requireActivity().application as HabitsApp).dataComponent.inject(this)

        ViewModelProvider(
            requireActivity(),
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HabitListViewModel(habitsUseCase) as T
                }
            })[HabitListViewModel::class.java]
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