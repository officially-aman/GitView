package com.aman.github_test.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aman.github_test.R
import com.aman.github_test.models.GitHubRepo

class RepoAdapter : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    private val repositories = mutableListOf<GitHubRepo>()

    fun addList(newList: List<GitHubRepo>) {
        val startPosition = repositories.size
        repositories.addAll(newList)
        notifyItemRangeInserted(startPosition, newList.size)
    }

    fun clearList() {
        repositories.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
        return RepoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    override fun getItemCount(): Int = repositories.size

    class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.repoName)
        private val description: TextView = itemView.findViewById(R.id.repoDescription)
        private val language: TextView = itemView.findViewById(R.id.repoLanguage)

        fun bind(repo: GitHubRepo) {
            name.text = repo.name
            description.text = repo.description ?: "No description"
            language.text = repo.language ?: "Unknown"
        }
    }
}
