package com.test.weather.utill

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomOnScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val callback: () -> Unit
) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()

            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                callback()
            }
        }
    }
}