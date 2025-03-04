package com.aman.github_test.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aman.github_test.R
import com.aman.github_test.data.ApiService
import com.aman.github_test.data.RetrofitClient
import com.aman.github_test.models.GitHubRepo
import com.aman.github_test.utils.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepoAdapter
    private lateinit var searchView: SearchView
    private lateinit var loadingDialog: LoadingDialog

    private val apiService = RetrofitClient.instance.create(ApiService::class.java)
    private var isLoading = false
    private var currentPage = 1
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        loadingDialog = LoadingDialog(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RepoAdapter()
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    username = query
                    currentPage = 1
                    adapter.clearList()
                    fetchRepositories(username, currentPage)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Pagination Listener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!isLoading && lastVisibleItemPosition + 3 >= totalItemCount) {
                    currentPage++
                    fetchRepositories(username, currentPage)
                }
            }
        })
    }

    private fun fetchRepositories(username: String, page: Int) {
        isLoading = true
        loadingDialog.show()

        apiService.getRepositories(username, page).enqueue(object : Callback<List<GitHubRepo>> {
            override fun onResponse(call: Call<List<GitHubRepo>>, response: Response<List<GitHubRepo>>) {
                loadingDialog.dismiss()
                isLoading = false
                when {
                    response.isSuccessful -> {
                        response.body()?.let {
                            adapter.addList(it)
                        }
                    }
                    response.code() == 403 -> {
                        showErrorDialog("API Rate Limit Exceeded", "You've made too many requests. Please wait and try again later.")
                    }
                    else -> {
                        showErrorDialog("Error", "Failed to load repositories. Please try again.")
                    }
                }
            }

            override fun onFailure(call: Call<List<GitHubRepo>>, t: Throwable) {
                loadingDialog.dismiss()
                isLoading = false
                showErrorDialog("Network Error", "Unable to fetch data: ${t.localizedMessage}")
            }
        })
    }

    private fun showErrorDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
