package com.blisschallenge.emojiapp.views.emojis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.ItemImageBinding
import com.blisschallenge.emojiapp.models.entities.ImageData

open class ImageAdapter : ListAdapter<ImageData, ImageAdapter.ViewHolder>(Companion) {

    lateinit var imageData: ImageData
    var onItemRemoved: (position: Int, changedList: List<ImageData>) -> Unit = { _: Int, _: List<ImageData> -> }

    private lateinit var viewHolder: ViewHolder

    inner class ViewHolder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.imageData.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            remove()
        }

        private fun remove() {

            if (adapterPosition != RecyclerView.NO_POSITION) {
                var listChange = currentList.toMutableList()

                if (currentList.size == 1) {
                    notifyItemRemoved(adapterPosition)
                    listChange = mutableListOf()
                }else {
                    listChange.removeAt(adapterPosition)
                }

                submitList(listChange)

                onItemRemoved(adapterPosition, listChange)
            }
        }
    }

    companion object : DiffUtil.ItemCallback<ImageData>() {

        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData) = oldItem.equals(newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemImageBinding>(inflater, R.layout.item_image, parent, false)

        binding.adapter = this
        viewHolder = ViewHolder(binding)

        return viewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Call/Assign values to data bindings and soon
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        imageData = getItem(position)

        holder.binding.executePendingBindings()
    }

    // Clean all elements of the recycler
    fun clear() {

        submitList(listOf())
        notifyDataSetChanged()
    }
}