package com.blisschallenge.emojiapp.extensions

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blisschallenge.emojiapp.R
import com.blisschallenge.emojiapp.helpers.DataState
import com.blisschallenge.emojiapp.helpers.DataState.*
import com.bumptech.glide.Glide

@BindingAdapter("gridOrientation", "gridColumns")
fun RecyclerView.bindLayoutOrientation(orientation: Int, columns: Int) {

    this.run {
        val gridLayout = GridLayoutManager(context, columns, orientation, false)
        this.layoutManager = gridLayout
    }
}

@BindingAdapter("imageUrl")
fun ImageView.bindUrl(url: String?) {

    if (url != null && url.isNotBlank()) {

        Glide.with(context)
            .load(url)
            .centerCrop()
            .error(R.drawable.ic_baseline_error_24)
            .into(this)
    }
}

@BindingAdapter("loadVisibility")
fun ImageView.toggleVisibility(loadingState: DataState?) {
    loadingState?.let {
        isInvisible = when(loadingState) {
            START -> true
            LOADED -> false
            NONE -> false
        }
    }
}

@BindingAdapter("loadVisibility")
fun ProgressBar.toggleVisibility(loadingState: DataState?) {
    loadingState?.let {
        isInvisible = when(loadingState) {
            START -> false
            LOADED -> true
            NONE -> true
        }
    }
}