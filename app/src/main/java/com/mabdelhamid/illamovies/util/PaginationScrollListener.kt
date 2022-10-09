package com.mabdelhamid.illamovies.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {
    // The minimum number of items to have below your current scroll position
    // before loading more.

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!isLoading() && (firstVisibleItem + visibleItemCount) >= totalItemCount && firstVisibleItem >= 0) {
            onLoadMore()
        }
    }

    // Defines the process for actually loading more data based on page
    // Returns true if more data is being loaded; returns false if there is no more data to load.
    abstract fun onLoadMore()

    abstract fun isLoading(): Boolean
}