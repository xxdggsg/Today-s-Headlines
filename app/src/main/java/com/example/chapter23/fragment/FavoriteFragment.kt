package com.example.chapter23.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chapter23.adapter.NewsAdapter
import com.example.chapter23.data.db.AppDatabase
import com.example.chapter23.data.repository.NewsRepository
import com.example.chapter23.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteUrls = mutableSetOf<String>()
    private val repository = NewsRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerviewFavorites.layoutManager = LinearLayoutManager(requireContext())

        val adapter = NewsAdapter(
            onItemClick = { news ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
                startActivity(intent)
            },
            onFavoriteClick = { news ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val db = AppDatabase.getInstance(requireContext())
                    withContext(Dispatchers.IO) { repository.removeFavorite(db, news.url) }
                    favoriteUrls.remove(news.url)
                    val favorites = withContext(Dispatchers.IO) { repository.getFavoriteNews(db) }
                    favoriteUrls.clear()
                    favoriteUrls.addAll(favorites.map { it.url })
                    (binding.recyclerviewFavorites.adapter as? NewsAdapter)?.submitList(favorites)
                    Toast.makeText(requireContext(), "已取消收藏", Toast.LENGTH_SHORT).show()
                }
            },
            favoriteUrls = favoriteUrls
        )
        binding.recyclerviewFavorites.adapter = adapter
        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val favorites = withContext(Dispatchers.IO) { repository.getFavoriteNews(db) }
            favoriteUrls.clear()
            favoriteUrls.addAll(favorites.map { it.url })
            (binding.recyclerviewFavorites.adapter as? NewsAdapter)?.submitList(favorites)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}