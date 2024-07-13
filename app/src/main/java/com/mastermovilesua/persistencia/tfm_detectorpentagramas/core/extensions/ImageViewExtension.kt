package com.mastermovilesua.persistencia.tfm_detectorpentagramas.core.extensions

import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.fromUri(uri: Uri) {
    Picasso.get().load(uri).into(this)
}

fun ImageView.fromUriScaleDown(uri: Uri, targetWidth: Int, targetHeight: Int) {
    Picasso.get()
        .load(uri)
        .resize(targetWidth, targetHeight)
        .onlyScaleDown()
        .centerCrop()
        .into(this)
}