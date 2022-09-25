package com.test.weather.ui.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.util.*

@BindingAdapter("setImage")
fun ImageView.setImage(position: Int) {
    val image =
        if ((position) % 2 == 0) "https://infotech.gov.ua/storage/img/Temp3.png" else "https://infotech.gov.ua/storage/img/Temp1.png"
    Glide.with(this).load(image).into(this)
}