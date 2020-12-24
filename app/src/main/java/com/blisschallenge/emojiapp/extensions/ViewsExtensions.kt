package com.blisschallenge.emojiapp.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blisschallenge.emojiapp.R
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