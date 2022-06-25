package com.example.countriesapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.countriesapp.R

fun ImageView.loadImage(url: String?) {
    url?.let {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_baseline_image)
            .apply(RequestOptions().override(600, 200).fitCenter())
            .into(this)
    }
}
