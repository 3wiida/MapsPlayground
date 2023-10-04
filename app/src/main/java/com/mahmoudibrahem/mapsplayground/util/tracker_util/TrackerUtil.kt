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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import java.text.DecimalFormat

object TrackerUtil {

    fun animateCameraToBounds(map: GoogleMap?, steps: List<LatLng>) {
        val bounds = LatLngBounds.builder()
        steps.forEach {
            bounds.include(it)
        }
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 200))
        map?.addMarker(MarkerOptions().position(steps.first()))
        map?.addMarker(MarkerOptions().position(steps.last()))
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
        }
    }

    @SuppressLint("MissingPermission")
    fun animateToDeviceLocation(
        map: GoogleMap?,
        locationProviderClient: FusedLocationProviderClient,
        onSuccess: () -> Unit
    ) {
        locationProviderClient.lastLocation.addOnSuccessListener {
            val devicePosition = LatLng(it.latitude, it.longitude)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(devicePosition, 17f))
            onSuccess.invoke()
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

    fun calculateDistance(startPosition: LatLng, endPosition: LatLng): String {
        val distanceInMeters = SphericalUtil.computeDistanceBetween(startPosition, endPosition)
        return DecimalFormat("#.##").format(distanceInMeters / 1000)
    }


}