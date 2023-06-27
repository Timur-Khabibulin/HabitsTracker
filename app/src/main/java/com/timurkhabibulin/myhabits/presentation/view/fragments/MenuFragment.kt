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
import com.timurkhabibulin.myhabits.databinding.FragmentMenuBinding
import com.timurkhabibulin.myhabits.databinding.ToolbarBinding
import com.timurkhabibulin.myhabits.presentation.HabitsApp
import com.timurkhabibulin.myhabits.presentation.viewmodel.HabitListViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class MenuFragment : Fragment() {
    @Inject
    lateinit var habitsUseCase: com.timurkhabibulin.domain.HabitsUseCase

    private var _binding: FragmentMenuBinding? = null
    private val binding
        get() = _binding!!

    private var toolbarBinding: ToolbarBinding? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createListFragmentViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        toolbarBinding = ToolbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDrawerToggle()
        setNavItemSelectedListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createListFragmentViewModel() {
        (requireActivity().application as HabitsApp).appComponent.inject(this)

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
            binding.menuLayout,
            binding.appBar.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.menuLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }


    private fun setNavItemSelectedListener() {
        binding.navView.setNavigationItemSelectedListener {
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