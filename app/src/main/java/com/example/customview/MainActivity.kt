package com.example.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.findNavController
import com.example.customview.databinding.ActivityMainBinding
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.donut_view_1 -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalAFragment())
                    true
                }
                R.id.donut_view_2 -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalBFragment())
                    true
                }
                R.id.bar_view -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalCFragment())
                    true
                }
                else -> throw IllegalArgumentException("Unknown Item")
            }
        }
    }
}