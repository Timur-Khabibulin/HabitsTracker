package com.timurkhabibulin.myhabits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.fragments.MENU_FRAGMENT_NAME
import com.timurkhabibulin.myhabits.fragments.MenuFargment
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.root_activity.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity() {
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        DynamicColors.applyToActivityIfAvailable(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.root_activity)

        this.savedInstanceState = savedInstanceState

        addFragment(MenuFargment.newInstance(), MENU_FRAGMENT_NAME)
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentFrame, fragment, tag)
                .setReorderingAllowed(true)
                //.addToBackStack(tag)
                .commit()
        }
    }

}