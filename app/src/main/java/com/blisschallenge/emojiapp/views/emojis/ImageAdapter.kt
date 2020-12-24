package com.blisschallenge.emojiapp.views.emojis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.ItemImageBinding

class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(Companion) {

    lateinit var url: String

    class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    companion object : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areItemsTheSame(oldItem: String, newItem: String) = areContentsTheSame(oldItem, newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemImageBinding>(inflater, R.layout.item_image, parent, false)

        binding.adapter = this

        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Call/Assign data bindings and soon
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        url = getItem(position)

        holder.binding.executePendingBindings()
    }
}