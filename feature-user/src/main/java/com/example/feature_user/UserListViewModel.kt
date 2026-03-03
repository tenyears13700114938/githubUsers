package com.example.feature_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.model.GithubUser
import com.example.data.repository.GithubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(private val repository: GithubUserRepository) :
    ViewModel() {
    val userListFlow: Flow<PagingData<GithubUser>> =
        repository.getUsers(pageSize = 30).cachedIn(viewModelScope)
}