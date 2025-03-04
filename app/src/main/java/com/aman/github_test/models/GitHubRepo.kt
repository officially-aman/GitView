package com.aman.github_test.models

data class GitHubRepo(
    val name: String,
    val description: String?,
    val language: String?,
    val stargazers_count: Int,
    val forks_count: Int
)
