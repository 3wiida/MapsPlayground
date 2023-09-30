package com.mahmoudibrahem.mapsplayground.ui.playground.playground_util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

object MarkerPlayground {
    fun addDefMarker(map: GoogleMap?, position: LatLng): Marker? {
        val defMarker = map?.addMarker(
            MarkerOptions().position(position)
        )
        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder().target(position).build()
            )
        )
        return defMarker
    }

    fun addBitmapMarker(map: GoogleMap?, resourceId: Int, position: LatLng): Marker? {
        val bitmapMarker = map?.addMarker(
            MarkerOptions().apply {
                position(position)
                icon(BitmapDescriptorFactory.fromResource(resourceId))
            }
        )
        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder().target(position).build()
            )
        )
        return bitmapMarker
    }

    fun addVectorMarker(
        context: Context,
        map: GoogleMap?,
        resourceId: Int,
        position: LatLng
    ): Marker? {
        val icon = convertVectorToBitmap(context, resourceId)
        val vectorMarker = map?.addMarker(
            MarkerOptions().apply {
                position(position)
                icon(icon)
            }
        )
        map?.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder().target(position).build()
            )
        )
        return vectorMarker
    }

    fun removeAllMarkers(markers: List<Marker>) {
        markers.forEach { marker -> marker.remove() }
    }

    private fun convertVectorToBitmap(context: Context, resourceId: Int): BitmapDescriptor {
        val vectorDrawable: Drawable = ResourcesCompat.getDrawable(
            context.resources,
            resourceId,
            null
        ) ?: return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}