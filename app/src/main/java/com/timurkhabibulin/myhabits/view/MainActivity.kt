package com.timurkhabibulin.myhabits.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.view.fragments.MENU_FRAGMENT_NAME
import com.timurkhabibulin.myhabits.view.fragments.MenuFargment
import kotlinx.android.synthetic.main.root_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        DynamicColors.applyToActivityIfAvailable(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.root_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(rootFragment.id, MenuFargment.newInstance(), MENU_FRAGMENT_NAME)
                .setReorderingAllowed(true)
                .commit()
        }
    }

}