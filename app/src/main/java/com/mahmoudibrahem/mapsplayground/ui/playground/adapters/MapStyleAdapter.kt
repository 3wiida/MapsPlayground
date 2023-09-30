package com.mahmoudibrahem.mapsplayground.ui.playground.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudibrahem.mapsplayground.databinding.MapStyleItemLayoutBinding
import com.mahmoudibrahem.mapsplayground.model.MapStyleItem

class MapStyleAdapter : ListAdapter<MapStyleItem, MapStyleAdapter.ItemViewHOlder>(Comparator) {

    var onSelectedItemChanged: ((MapStyleItem) -> Unit)? = null

    inner class ItemViewHOlder(
        private val binding: MapStyleItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MapStyleItem) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHOlder {
        val binding = MapStyleItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHOlder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHOlder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onSelectedItemChanged?.invoke(item) }
    }

    private object Comparator : DiffUtil.ItemCallback<MapStyleItem>() {
        override fun areItemsTheSame(oldItem: MapStyleItem, newItem: MapStyleItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MapStyleItem, newItem: MapStyleItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}