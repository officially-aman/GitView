package com.aman.github_test.data

import com.aman.github_test.models.GitHubRepo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users/{username}/repos")
    fun getRepositories(
        @Path("username") username: String,
        @Query("page") page: Int,  // For pagination
        @Query("per_page") perPage: Int = 10 // Number of repositories per page
    ): Call<List<GitHubRepo>>

}
