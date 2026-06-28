package com.example.chapter23.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chapter23.adapter.NewsAdapter
import com.example.chapter23.data.db.AppDatabase
import com.example.chapter23.data.repository.NewsRepository
import com.example.chapter23.databinding.FragmentHomeBinding
import com.example.chapter23.ui.viewmodel.NewsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val favoriteUrls = mutableSetOf<String>()
    private val repository = NewsRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewNews.layoutManager = LinearLayoutManager(requireContext())

        // 初始化收藏集合
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val favorites = withContext(Dispatchers.IO) { repository.getFavoriteNews(db) }
            favoriteUrls.addAll(favorites.map { it.url })
        }

        val adapter = NewsAdapter(
            onItemClick = { news ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                startActivity(intent)
            },
            onFavoriteClick = { news ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val db = AppDatabase.getInstance(requireContext())
                    val isFav = withContext(Dispatchers.IO) { repository.isFavorite(db, news.url) }
                    if (isFav) {
                        withContext(Dispatchers.IO) { repository.removeFavorite(db, news.url) }
                        favoriteUrls.remove(news.url)
                        Toast.makeText(requireContext(), "已取消收藏", Toast.LENGTH_SHORT).show()
                    } else {
                        withContext(Dispatchers.IO) { repository.addFavorite(db, news) }
                        favoriteUrls.add(news.url)
                        Toast.makeText(requireContext(), "已收藏", Toast.LENGTH_SHORT).show()
                    }
                    // 通过 submitList 提交新列表，触发局部刷新
                    val currentAdapter = binding.recyclerviewNews.adapter as? NewsAdapter
                    val currentList = currentAdapter?.currentList?.toList() ?: emptyList()
                    currentAdapter?.submitList(currentList)
                }
            },
            favoriteUrls = favoriteUrls
        )
        binding.recyclerviewNews.adapter = adapter

        val viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        viewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            adapter.submitList(newsList)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchNews()
        }

        binding.swipeRefresh.isRefreshing = true
        viewModel.fetchNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}