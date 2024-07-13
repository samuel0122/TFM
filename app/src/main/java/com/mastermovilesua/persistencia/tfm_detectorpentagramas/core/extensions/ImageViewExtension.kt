package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions

import android.icu.text.ListFormatter.Width
import android.net.Uri
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso

fun AppCompatImageView.fromUri(uri: Uri) {
    Picasso.get().load(uri).into(this)
}

fun AppCompatImageView.fromUriScaleDown(uri: Uri, targetWidth: Int, targetHeight: Int) {
    Picasso.get()
        .load(uri)
        .resize(targetWidth, targetHeight)
        .onlyScaleDown()
        .centerCrop()
        .into(this)
}