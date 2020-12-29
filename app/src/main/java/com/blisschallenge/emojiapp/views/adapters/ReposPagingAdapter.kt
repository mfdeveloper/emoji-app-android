package com.blisschallenge.emojiapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.ItemGitRepoBinding
import com.blisschallenge.emojiapp.models.entities.Repo

open class ReposPagingAdapter : PagingDataAdapter<Repo, ReposPagingAdapter.ViewHolder>(Companion) {

    var repo: Repo? = null
    private lateinit var viewHolder: ViewHolder

    inner class ViewHolder(val binding: ItemGitRepoBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<Repo>() {

        override fun areItemsTheSame(oldItem: Repo, newItem: Repo) = oldItem.fullName == newItem.fullName
        override fun areContentsTheSame(oldItem: Repo, newItem: Repo) = oldItem.equals(newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemGitRepoBinding>(inflater, R.layout.item_git_repo, parent, false)

        binding.adapter = this
        viewHolder = ViewHolder(binding)

        return viewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Call/Assign values to data bindings and soon
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // TODO: Refactor this and create a bind() method in ViewHolder class that shows a placeholder when item is null
        repo = getItem(position)

        if (repo != null) {
            holder.binding.executePendingBindings()
        }
    }
}