package com.timurkhabibulin.myhabits.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.timurkhabibulin.myhabits.databinding.RootActivityBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: RootActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = RootActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}