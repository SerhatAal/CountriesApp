package com.example.countriesapp.utils

import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.countriesapp.R

fun ImageView.loadUrl(url: String?) {

    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(R.drawable.ic_baseline_image)
        .error(R.drawable.ic_baseline_error)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}