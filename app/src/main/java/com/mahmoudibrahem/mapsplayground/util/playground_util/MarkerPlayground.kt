package com.mahmoudibrahem.mapsplayground.util.playground_util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mahmoudibrahem.mapsplayground.model.LocationData
import com.mahmoudibrahem.mapsplayground.ui.adapters.CustomInfoWindowAdapter
import java.util.Locale

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

    fun enableMarkerDrag(isEnable: Boolean, markers: List<Marker>) {
        markers.forEach { marker ->
            marker.isDraggable = isEnable
        }
    }


    fun getLocationInfo(
        location: LatLng,
        context: Context,
        locale: Locale,
        callback: (Address?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Geocoder(context, locale).getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) { result ->
                if(result.size>0){
                    callback.invoke(result.first())
                }else{
                    callback.invoke(null)
                }
            }
        } else {
            val result = Geocoder(context, locale).getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            result?.let {
                if(it.size>0){
                    callback.invoke(it.first())
                }else{
                    callback.invoke(null)
                }
            }
        }
    }

}