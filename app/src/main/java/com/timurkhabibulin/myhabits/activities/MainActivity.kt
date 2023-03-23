package com.timurkhabibulin.myhabits.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.R
import com.timurkhabibulin.myhabits.fragments.AboutAppFragment
import com.timurkhabibulin.myhabits.fragments.HabitListDisplayMode
import com.timurkhabibulin.myhabits.fragments.HomeFragment
import kotlinx.android.synthetic.main.root_activity.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.root_activity)

        this.savedInstanceState = savedInstanceState

        setUpDrawerToggle()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addFragment(HomeFragment.newInstance(HabitListDisplayMode.GOOD_HABITS))

        setNavItemSelectedListener()
    }

    private fun setUpDrawerToggle() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            root_layout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        root_layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun addFragment(fragment: Fragment) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.content_frame, fragment)
                .commit()
        }
    }

    private fun setNavItemSelectedListener() {
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> replaceFragment(HomeFragment.newInstance(HabitListDisplayMode.GOOD_HABITS))
                R.id.menu_about -> replaceFragment(AboutAppFragment.newInstance())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) true
        return super.onOptionsItemSelected(item)
    }

}