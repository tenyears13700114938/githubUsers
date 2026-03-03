package com.example.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.api.GithubService
import com.example.data.model.GithubUser

class GithubUserPagingSource(
    private val service: GithubService
) : PagingSource<Int, GithubUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        val position = params.key ?: 0

        return try {
            val response = service.getUsers(since = position, perPage = params.loadSize)

            LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = if (response.isEmpty()) null else response.last().id
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}