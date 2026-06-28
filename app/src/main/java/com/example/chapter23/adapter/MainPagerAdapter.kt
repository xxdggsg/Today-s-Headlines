package com.example.chapter23.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chapter23.fragment.FavoriteFragment
import com.example.chapter23.fragment.HomeFragment
import com.example.chapter23.fragment.SettingsFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> HomeFragment()
        1 -> FavoriteFragment()
        2 -> SettingsFragment()
        else -> HomeFragment()
    }
}