package com.mahmoudibrahem.mapsplayground.util.tracker_util

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.ButtCap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions

object TrackerUtil {

    fun animateCameraToBounds(map: GoogleMap?, steps: List<LatLng>) {
        val bounds = LatLngBounds.builder()
        steps.forEach {
            bounds.include(it)
        }
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
    }

    fun drawTrackPolyLine(map: GoogleMap?, steps: List<LatLng>) {
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(steps.last(), 17f))
        map?.addPolyline(
            PolylineOptions().apply {
                addAll(steps)
                color(Color.GREEN)
                startCap(ButtCap())
                endCap(ButtCap())
                jointType(JointType.ROUND)
                width(10f)
            }
        )
    }

    fun initTrackMap(map: GoogleMap?) {
        map?.uiSettings?.apply {
            this.isCompassEnabled = false
            this.isMapToolbarEnabled = false
            this.isMyLocationButtonEnabled = true
        }
    }

    @SuppressLint("MissingPermission")
    fun animateToDeviceLocation(
        map: GoogleMap?,
        locationProviderClient: FusedLocationProviderClient
    ) {
        locationProviderClient.lastLocation.addOnSuccessListener {
            val devicePosition = LatLng(it.latitude, it.longitude)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(devicePosition, 17f))
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(
        locationClient: FusedLocationProviderClient,
        onUpdate: LocationCallback,
    ) {
        locationClient.requestLocationUpdates(
            buildLocationRequest(),
            onUpdate,
            Looper.getMainLooper()
        )
    }

    private fun buildLocationRequest(): LocationRequest {
        return LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()
    }

}