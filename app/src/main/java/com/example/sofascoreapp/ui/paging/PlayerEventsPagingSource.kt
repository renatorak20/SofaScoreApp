package com.example.sofascoreapp.ui.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sofascoreapp.data.networking.Network
import kotlin.math.abs


class PlayerEventsPagingSource(private val playerID: Int) : PagingSource<Int, Any>() {

    override fun getRefreshKey(state: PagingState<Int, Any>): Int {
        return (state.anchorPosition ?: 0) / state.config.pageSize
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val key = params.key ?: 0
        val span = if (key > 0) "next" else "last"

        val response = Network().getService().getPlayerEventsPage(playerID, span, abs(key))
        val allEvents = response.body() ?: emptyList()

        val groupedMatches =
            allEvents.groupBy { it.tournament }.flatMap { listOf(it.key) + it.value }

        val prevKey = if (allEvents.isNotEmpty()) key - 1 else null
        val nextKey = if (allEvents.isNotEmpty()) key + 1 else null

        return LoadResult.Page(
            data = groupedMatches,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }


}