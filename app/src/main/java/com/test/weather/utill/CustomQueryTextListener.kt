package com.test.weather.utill

import android.widget.SearchView

open class CustomQueryTextListener(private val callBack: (String) -> Unit) : SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(newText: String?): Boolean {
        callBack(newText.toString())
        return true
    }

}