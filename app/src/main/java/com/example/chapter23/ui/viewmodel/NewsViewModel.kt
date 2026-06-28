package com.example.chapter23.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chapter23.data.model.News
import com.example.chapter23.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()
    private val _newsList = MutableLiveData<List<News>>()
    val newsList: LiveData<List<News>> = _newsList

    fun fetchNews() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.fetchNewsFromNetwork()
            }
            if (result != null) {
                _newsList.value = result
            }
        }
    }
}