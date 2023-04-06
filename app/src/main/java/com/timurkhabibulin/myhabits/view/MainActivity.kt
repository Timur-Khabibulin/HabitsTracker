package com.timurkhabibulin.myhabits.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.view.fragments.MENU_FRAGMENT_NAME
import com.timurkhabibulin.myhabits.view.fragments.MenuFargment


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
                .commit()
        }
    }

}