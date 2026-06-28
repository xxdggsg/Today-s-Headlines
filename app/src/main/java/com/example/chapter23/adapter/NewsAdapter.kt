package com.example.chapter23.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chapter23.data.model.News
import com.example.chapter23.databinding.ItemNewsBinding

class NewsAdapter(
    private val onItemClick: (News) -> Unit,
    private val onFavoriteClick: (News) -> Unit,
    private val favoriteUrls: Set<String>   // 已收藏的 url 集合
) : ListAdapter<News, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) {
            binding.tvTitle.text = news.title
            binding.tvSource.text = news.source
            binding.tvDate.text = news.date

            // 根据 url 是否在 favoriteUrls 中设置按钮颜色和文字
            val isFav = favoriteUrls.contains(news.url)
            if (isFav) {
                binding.tvFavorite.text = "★已收藏"
                binding.tvFavorite.setTextColor(Color.parseColor("#FFD700"))  // 黄色
            } else {
                binding.tvFavorite.text = "☆收藏"
                binding.tvFavorite.setTextColor(Color.parseColor("#000000"))  // 白色
            }

            binding.root.setOnClickListener { onItemClick(news) }
            binding.tvFavorite.setOnClickListener { onFavoriteClick(news) }
        }
    }
}

class NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
        oldItem.url == newItem.url
    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
        oldItem == newItem
}