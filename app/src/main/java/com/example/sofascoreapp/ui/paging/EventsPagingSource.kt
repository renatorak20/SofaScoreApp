package com.example.sofascoreapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sofascoreapp.data.model.Event

class EventsPagingSource : PagingSource<Int, Event>() {

    override fun getRefreshKey(state: PagingState<Int, Event>): Int? {
        return (state.anchorPosition ?: 0) / state.config.pageSize
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Event> {
        TODO("Not yet implemented")
    }
}