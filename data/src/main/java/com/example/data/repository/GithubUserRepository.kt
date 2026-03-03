package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.data.api.GithubService
import com.example.data.model.GithubUser
import com.example.data.model.GithubUserDetail
import com.example.data.repository.paging.GithubUserPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GithubUserRepository(private val service: GithubService) {
    fun getUsers(pageSize: Int): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, initialLoadSize = pageSize),
            pagingSourceFactory = { GithubUserPagingSource(service) }
        ).flow
    }

    fun getUserDetail(userName: String): Flow<Resource<GithubUserDetail>> = flow {
        emit(Resource.Loading)
        try {
            val userDetail = service.getUserDetail(userName)
            emit(Resource.Success(userDetail))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }
}