package com.example.chapter23

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chapter23.adapter.MainPagerAdapter
import com.example.chapter23.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.adapter = MainPagerAdapter(this)

        binding.btnHome.setOnClickListener { binding.viewpager.currentItem = 0 }
        binding.btnFavorite.setOnClickListener { binding.viewpager.currentItem = 1 }
        binding.btnSettings.setOnClickListener { binding.viewpager.currentItem = 2 }
    }
}