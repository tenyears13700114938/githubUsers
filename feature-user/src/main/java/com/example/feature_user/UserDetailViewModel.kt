package com.example.feature_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.GithubUserDetail
import com.example.data.repository.GithubUserRepository
import com.example.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: GithubUserRepository,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val userName: String? = savedStateHandle[USER_NAME]

    val userDetailState: StateFlow<UserDetailState> = repository.getUserDetail(userName ?: "").map {
        when (it) {
            is Resource.Loading -> UserDetailState.Loading
            is Resource.Success -> UserDetailState.Success(it.data)
            is Resource.Error -> UserDetailState.Error(it.exception.message ?: "Unknown error")
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserDetailState.Loading)


    companion object {
        const val USER_NAME = "userName"
    }
}

sealed class UserDetailState() {
    data object Loading : UserDetailState()
    data class Success(val data: GithubUserDetail) : UserDetailState()
    data class Error(val message: String) : UserDetailState()
}
