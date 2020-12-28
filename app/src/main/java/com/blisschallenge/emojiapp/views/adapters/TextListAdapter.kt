package com.blisschallenge.emojiapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.databinding.ItemTextBinding
import com.blisschallenge.emojiapp.models.entities.TextData

open class TextListAdapter : ListAdapter<TextData, TextListAdapter.ViewHolder>(Companion) {

    lateinit var textData: TextData
    var onItemRemoved: (position: Int, changedList: List<TextData>) -> Unit = { _: Int, _: List<TextData> -> }

    private lateinit var viewHolder: ViewHolder

    inner class ViewHolder(val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.textData.setOnClickListener(this)
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

    companion object : DiffUtil.ItemCallback<TextData>() {

        override fun areItemsTheSame(oldItem: TextData, newItem: TextData) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TextData, newItem: TextData) = oldItem.equals(newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemTextBinding>(inflater, R.layout.item_text, parent, false)

        binding.adapter = this
        viewHolder = ViewHolder(binding)

        return viewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Call/Assign values to data bindings and soon
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        textData = getItem(position)

        holder.binding.executePendingBindings()
    }

    // Clean all elements of the recycler
    fun clear() {

        submitList(listOf())
        notifyDataSetChanged()
    }
}