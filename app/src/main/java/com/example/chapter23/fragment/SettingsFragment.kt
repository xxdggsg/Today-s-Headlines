package com.example.chapter23.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.chapter23.data.db.AppDatabase
import com.example.chapter23.data.repository.NewsRepository
import com.example.chapter23.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val repository = NewsRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClearCache.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("确认清除")
                .setMessage("确定要清除所有收藏的新闻吗？")
                .setPositiveButton("确定") { _, _ -> clearFavorites() }
                .setNegativeButton("取消", null)
                .show()
        }

        binding.btnAbout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("关于")
                .setMessage("新闻客户端 v1.0\n基于 Kotlin + MVVM + Room + Retrofit")
                .setPositiveButton("确定", null)
                .show()
        }
    }

    private fun clearFavorites() {
        lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            withContext(Dispatchers.IO) { repository.clearFavorites(db) }
            Toast.makeText(requireContext(), "缓存已清除", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}