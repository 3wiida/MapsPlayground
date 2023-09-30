package com.mahmoudibrahem.mapsplayground.ui.playground.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudibrahem.mapsplayground.databinding.MapTypeItemLayoutBinding
import com.mahmoudibrahem.mapsplayground.model.MapTypeItem

class MapTypeAdapter : ListAdapter<MapTypeItem, MapTypeAdapter.ItemViewHOlder>(Comparator) {

    var onSelectedItemChanged: ((MapTypeItem) -> Unit)? = null

    inner class ItemViewHOlder(
        private val binding: MapTypeItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MapTypeItem) {
            binding.item = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHOlder {
        val binding = MapTypeItemLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHOlder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHOlder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onSelectedItemChanged?.invoke(item) }
    }

    private object Comparator : DiffUtil.ItemCallback<MapTypeItem>() {
        override fun areItemsTheSame(oldItem: MapTypeItem, newItem: MapTypeItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MapTypeItem, newItem: MapTypeItem): Boolean {
            return oldItem.id == newItem.id
        }
    }
}