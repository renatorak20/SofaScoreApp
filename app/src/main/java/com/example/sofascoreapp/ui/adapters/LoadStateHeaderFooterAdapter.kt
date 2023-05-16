package com.example.sofascoreapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sofascoreapp.databinding.PagingLoaderBinding

class LoadStateHeaderFooterAdapter :
    LoadStateAdapter<LoadStateHeaderFooterAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(
        holder: LoadStateHeaderFooterAdapter.LoaderViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateHeaderFooterAdapter.LoaderViewHolder {
        return LoaderViewHolder(
            PagingLoaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class LoaderViewHolder(private val binding: PagingLoaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.indicator.isVisible = loadState is LoadState.Loading
        }
    }

}