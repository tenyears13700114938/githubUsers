package com.example.data.api

import com.example.data.model.GithubUser
import com.example.data.model.GithubUserDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") perPage: Int
    ): List<GithubUser>

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): GithubUserDetail
}