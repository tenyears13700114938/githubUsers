package com.example.data.model

data class GithubUserDetail(
    val id: Int,
    val login: String,
    val avatar_url: String,
    val html_url: String,
    val type: String,
    val email: String,
    val blog: String,
    val location: String?,
    val public_repos: Int,
    val followers: Int,
    val following: Int
)