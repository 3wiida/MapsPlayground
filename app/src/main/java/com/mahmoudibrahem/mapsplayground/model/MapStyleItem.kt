package com.mahmoudibrahem.mapsplayground.model

data class MapStyleItem(
    val name: String,
    val id: Int,
    val isSelected: Boolean = false,
    val stylePath:Int
)
