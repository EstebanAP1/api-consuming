package com.example.api_consuming.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api_consuming.model.Post
import com.example.api_consuming.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PostViewModel: ViewModel() {
    private val _uiState = mutableStateOf<UiState>(UiState.Loading)
    val uiState: State<UiState> get() = _uiState

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch {
            _uiState.value = try {
                val response = RetrofitInstance.api.getPosts()
                if (response.isSuccessful) {
                    UiState.Success(response.body() ?: emptyList())
                } else {
                    UiState.Error("Error ${response.code()}")
                }
            } catch (e: IOException) {
                UiState.Error("Network Error")
            } catch (e: HttpException) {
                UiState.Error("HTTP Error")
            }
        }
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Success(val posts: List<Post>) : UiState()
    data class Error(val message: String) : UiState()
}