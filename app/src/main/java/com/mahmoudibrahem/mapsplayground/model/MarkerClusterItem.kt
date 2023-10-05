package com.mahmoudibrahem.mapsplayground.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MarkerClusterItem(
    private val position: LatLng
) : ClusterItem {
    override fun getPosition(): LatLng = position
    override fun getTitle(): String? = null

    override fun getSnippet(): String? = null

    override fun getZIndex(): Float? = null
}